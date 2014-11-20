package org.unique.web.handler.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.unique.web.annotation.Action.HttpMethod;
import org.unique.web.core.Action;
import org.unique.web.core.ActionInvocation;
import org.unique.web.core.ActionMapping;
import org.unique.web.exception.ActionException;
import org.unique.web.handler.Handler;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;

/**
 * 默认的Handler实现
 * @author biezhi
 * @since 1.0
 */
public final class DefalutHandlerImpl implements Handler {

    private final ActionMapping actionMapping = ActionMapping.single();

    private static final RenderFactory renderFactory = RenderFactory.single();

    private Logger logger = Logger.getLogger(DefalutHandlerImpl.class);

    @Deprecated
    public DefalutHandlerImpl() {
    }
    
    public static DefalutHandlerImpl create(){
    	return new DefalutHandlerImpl();
    }

    public final boolean handle(String target, HttpServletRequest request, HttpServletResponse response) {
    	if(target.endsWith("/")){
    		target = target.substring(0, target.length() - 1);
		}
    	// 伪静态
		if(target.endsWith(Render.suffix)){
			target = target.substring(0, target.length() - Render.suffix.length());
		} else{
			// 不处理静态资源
			if (target.indexOf(".") != -1) {
	            return false;
	        }
		}

        logger.debug("reuqest:[" + target + "]");
        
        // 获取路由
        Action action = actionMapping.getAction(target);
        
        if (action == null) {
            String qs = request.getQueryString();
            logger.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
            renderFactory.getErrorRender(404).render(request, response, null);
            return true;
        }
        try {
            // 验证是否
            if (!verifyMethod(action.getMethodType(), request.getMethod())) {
                logger.warn("404 Error request method");
                renderFactory.getErrorRender(404).render(request, response, null);
                return true;
            }
            String nameSpace = action.getControllerClass().getAnnotation(org.unique.web.annotation.Controller.class).value();
            Object result = new ActionInvocation(action, nameSpace).invoke();
            if(null != result && result instanceof String){
            	nameSpace = nameSpace.startsWith("/") ? nameSpace.substring(1) : nameSpace;
            	String viewPath = nameSpace + result;
            	renderFactory.getDefaultRender().render(request, response, viewPath);
            }
            return true;
        } catch (ActionException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 404) {
                String qs = request.getQueryString();
                logger.warn("404 Not Found: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 401) {
                String qs = request.getQueryString();
                logger.warn("401 Unauthorized: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 403) {
                String qs = request.getQueryString();
                logger.warn("403 Forbidden: " + (qs == null ? target : target + "?" + qs));
            }
            e.getErrorRender().render(request, response, null);
        } catch (Exception e) {
            logger.warn("Exception: " + e.getMessage());
            renderFactory.getErrorRender(500).render(request, response, null);
        }
        return false;
    }

    /**
     * 验证请求方法
     * @param methodType 请求类型
     * @param method 方法
     * @return 验证成功/失败
     */
    private boolean verifyMethod(HttpMethod methodType, String method) {
        if (null == methodType || methodType == HttpMethod.ALL) {
            return true;
        }
        if (methodType == HttpMethod.GET) {
            return method.trim().equals(HttpMethod.GET.toString());
        }
        if (methodType == HttpMethod.POST) {
            return method.trim().equals(HttpMethod.POST.toString());
        }
        if (methodType == HttpMethod.PUT) {
            return method.trim().equals(HttpMethod.PUT.toString());
        }
        if (methodType == HttpMethod.DELETE) {
            return method.trim().equals(HttpMethod.DELETE.toString());
        }
        return false;
    }

}

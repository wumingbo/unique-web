package org.unique.web.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.unique.Const;
import org.unique.Unique;
import org.unique.tools.WebUtil;
import org.unique.web.handler.Handler;

/**
 * mvc核心过滤器
 * @author biezhi
 * @since 1.0
 */
@WebFilter(urlPatterns = {"/*"}, 
asyncSupported = true,
initParams = {@WebInitParam(name="configPath",value=Const.CUSTOM_CONFIG)})
public class Dispatcher implements Filter {

	private Logger logger = Logger.getLogger(Dispatcher.class);

	private static Unique unique = Unique.single();

	private static boolean isInit = false;

	private static Handler handler;

	/**
	 * 初始化项目
	 * @param config 初始化参数对象
	 */
	public void init(FilterConfig config) {
		long start = System.currentTimeMillis();
		final String configPath = config.getInitParameter("configPath");
		if (!isInit) {
			logger.info("Root WebApplicationContext: 初始化开始...");
			
			// config path
			ActionContext.setActionContext(config.getServletContext());
			
			// init web
			isInit = unique.init(configPath);
			
			handler = unique.getHandler();
			long time = (System.currentTimeMillis() - start);
			logger.info("Root WebApplicationContext: 初始化耗费时长：" + time + " ms");
			logger.info("unique-web初始化完成!");
		}
	}

	/**
	 * 执行过滤器
	 * @param req 请求对象
	 * @param res 响应对象
	 * @param chain filterchain
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		// 相对于系统根目录的访问的路径
		String target = WebUtil.getRelativePath(request, "");

		// set reqest and response
		ActionContext.setActionContext(request.getServletContext(), request, response);
		if (!handler.handle(target, request, response)) {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 销毁方法
	 */
	public void destroy() {
		if (null != unique) {
			unique = null;
		}
		if (null != handler) {
			handler = null;
		}
	}

}

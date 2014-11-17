package org.unique.web.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 顶层渲染器接口
 * @author biezhi
 * @version 0.1.0
 */
public abstract class Render {

    /**
     * 渲染视图方法
     * @param request 请求对象
     * @param response 响应对象
     * @param viewPath 视图位置
     */
	public abstract void render(HttpServletRequest request, HttpServletResponse response, String viewPath);
	
}

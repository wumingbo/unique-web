package org.unique.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.web.render.Render;

/**
 * 请求渲染器
 * @author　biezhi
 * @version　1.0
 */
public final class ActionRender extends Render {
	
	private String actionUrl;
	
	public ActionRender(String actionUrl) {
		this.actionUrl = actionUrl.trim();
	}
	
	public String getActionUrl() {
		return actionUrl;
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
	}
	
}

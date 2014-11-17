package org.unique.web.render.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;
import org.unique.web.render.Render;

/**
 * JspRender.
 */
public class JspRender extends Render {

	public JspRender() {
		// TODO Auto-generated constructor stub
	}
	
	public JspRender(String view) {
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
		try {
			if(!viewPath.endsWith(".jsp")){
				viewPath += ".jsp";
			}
			response.setCharacterEncoding(Const.ENCODING);
			request.getRequestDispatcher(viewPath).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
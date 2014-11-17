package org.unique.support.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;
import org.unique.web.render.Render;

/**
 * jsp渲染器
 * @author biezhi
 * @since 1.0
 */
public class JspRender implements Render {

	private static final String suffix = ".jsp";
	
	public JspRender() {
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
		try {
			String url = prefix + viewPath + suffix;
			if(viewPath.endsWith(suffix)){
				url = prefix + viewPath;
			}
			url = url.replaceAll("//", "/");
			response.setCharacterEncoding(Const.ENCODING);
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
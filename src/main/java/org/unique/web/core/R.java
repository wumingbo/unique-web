package org.unique.web.core;

import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unique.tools.StringUtils;
import org.unique.web.annotation.Controller;
import org.unique.web.exception.ActionException;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;

/**
 * http请求处理的工具类
 * @author biezhi
 * @since 1.0
 */
public final class R {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Set<String> headers = null;
	private String[] urlPara;
	private Render defaultRender;
	private String viewPath;
	private static final RenderFactory renderFactory = RenderFactory.single();
	
	public R(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 返回pathinfo
	 * @return path info Example return: "/example/foo"
	 */
	public String pathInfo() {
		return request.getPathInfo();
	}

	/**
	 * 返回servletPath
	 * @return the servlet path
	 */
	public String servletPath() {
		return request.getServletPath();
	}

	/**
	 * 返回contextPath
	 * @return the context path
	 */
	public String contextPath() {
		return request.getContextPath();
	}

	/**
	 * 根据header获取请求头信息
	 * 
	 * @param header 请求头的键
	 * @return
	 */
	public String headers(String header) {
		return request.getHeader(header);
	}

	/**
	 * 所有请求头
	 * 
	 * @return all headers
	 */
	public Set<String> headers() {
		if (headers == null) {
			headers = new TreeSet<String>();
			Enumeration<String> enumeration = request.getHeaderNames();
			while (enumeration.hasMoreElements()) {
				headers.add(enumeration.nextElement());
			}
		}
		return headers;
	}

	public String queryString() {
		return request.getQueryString();
	}

	/**
	 * 获取string类型请求参数
	 * 
	 * @param name 参数名
	 * @return 参数值
	 */
	public String getPara(String name) {
		return request.getParameter(name);
	}

	/**
	 * 获取string请求参数，如果没有给一个默认值
	 * 
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public String getPara(String name, String defaultValue) {
		String result = request.getParameter(name);
		return result != null && !"".equals(result) ? result : defaultValue;
	}

	public Map<String, String[]> getParaMap() {
		return request.getParameterMap();
	}

	public Enumeration<String> getParaNames() {
		return request.getParameterNames();
	}

	public String[] getParaValues(String name) {
		return request.getParameterValues(name);
	}

	public Integer[] getParaValuesToInt(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null)
			return null;
		Integer[] result = new Integer[values.length];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.parseInt(values[i]);
		return result;
	}

	public Enumeration<String> getAttrNames() {
		return request.getAttributeNames();
	}

	public void setAttr(String attribute, Object value) {
		request.setAttribute(attribute, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttr(String name) {
		Object attr = request.getAttribute(name);
		if (null == attr) {
			return null;
		}
		return (T) request.getAttribute(name);
	}

	public String getAttrForStr(String name) {
		return (String) request.getAttribute(name);
	}

	public Integer getAttrForInt(String name) {
		return (Integer) request.getAttribute(name);
	}

	private Integer toInt(String value, Integer defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Integer.parseInt(value.substring(1));
		return Integer.parseInt(value);
	}

	public Integer getParaToInt(String name) {
		return toInt(request.getParameter(name), null);
	}

	public Integer getParaToInt(String name, Integer defaultValue) {
		return toInt(request.getParameter(name), defaultValue);
	}

	private Long toLong(String value, Long defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Long.parseLong(value.substring(1));
		return Long.parseLong(value);
	}

	public Long getParaToLong(String name) {
		return toLong(request.getParameter(name), null);
	}

	public Long getParaToLong(String name, Long defaultValue) {
		return toLong(request.getParameter(name), defaultValue);
	}

	private Boolean toBoolean(String value, Boolean defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		value = value.trim().toLowerCase();
		if ("1".equals(value) || "true".equals(value)) {
			return Boolean.TRUE;
		} else if ("0".equals(value) || "false".equals(value)) {
			return Boolean.FALSE;
		}
		throw new RuntimeException("Can not parse the parameter \"" + value + "\" to boolean value.");
	}

	public Boolean getParaToBoolean(String name) {
		return toBoolean(request.getParameter(name), null);
	}

	public Boolean getParaToBoolean(String name, Boolean defaultValue) {
		return toBoolean(request.getParameter(name), defaultValue);
	}

	public Boolean getParaToBoolean() {
		return toBoolean(getPara(), null);
	}

	private Date toDate(String value, Date defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		try {
			return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public Date getParaToDate(String name) {
		return toDate(request.getParameter(name), null);
	}

	public Date getParaToDate(String name, Date defaultValue) {
		return toDate(request.getParameter(name), defaultValue);
	}

	public Date getParaToDate() {
		return toDate(getPara(), null);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}

	public R setSessionAttr(String key, Object value) {
		request.getSession().setAttribute(key, value);
		return this;
	}

	public R removeSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.removeAttribute(key);
		return this;
	}

	public String getCookie(String name, String defaultValue) {
		Cookie cookie = getCookieObject(name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}

	public String getCookie(String name) {
		return getCookie(name, null);
	}

	public Integer getCookieToInt(String name) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : null;
	}

	public Integer getCookieToInt(String name, Integer defaultValue) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}

	public Long getCookieToLong(String name) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : null;
	}

	public Long getCookieToLong(String name, Long defaultValue) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : defaultValue;
	}

	public Cookie getCookieObject(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}

	public Cookie[] getCookieObjects() {
		Cookie[] result = request.getCookies();
		return result != null ? result : new Cookie[0];
	}

	public R setCookie(Cookie cookie) {
		response.addCookie(cookie);
		return this;
	}

	public R setCookie(String name, String value, int maxAgeInSeconds, String path) {
		setCookie(name, value, maxAgeInSeconds, path, null);
		return this;
	}

	public R setCookie(String name, String value, int maxAgeInSeconds, String path, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null)
			cookie.setDomain(domain);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		response.addCookie(cookie);
		return this;
	}

	public R setCookie(String name, String value, int maxAgeInSeconds) {
		setCookie(name, value, maxAgeInSeconds, "/", null);
		return this;
	}

	public R removeCookie(String name) {
		setCookie(name, null, 0, "/", null);
		return this;
	}

	public R removeCookie(String name, String path) {
		setCookie(name, null, 0, path, null);
		return this;
	}

	/**
	 * 移除cookie
	 * @param name cookie名称
	 * @param path cookie path
	 * @param domain cookie所在域
	 * @return R对象
	 */
	public R removeCookie(String name, String path, String domain) {
		setCookie(name, null, 0, path, domain);
		return this;
	}

	/**
	 * 获取url第一个参数
	 * @return 第一个参数的值
	 */
	public String getPara() {
		if (null == urlPara || urlPara.length == 0) {
			return null;
		}
		return urlPara[0].toString();
	}
	
	public Object[] getUrlPara() {
		return urlPara;
	}

	/**
	 * 获取int类型的参数
	 * @return 参数值
	 */
	public Integer getParaToInt() {
		return toInt(getPara(), null);
	}

	/**
	 * 获取long类型的参数
	 * @return 参数值
	 */
	public Long getParaToLong() {
		return toLong(getPara(), null);
	}
	
	public void render(final Render render) {
		render.render(request, response, viewPath);
	}
	
	public void render(final String view) {
		renderFactory.getDefaultRender().render(request, response, view);
	}
	
	public void render(Object object, final String view) {
		if(null == object.getClass().getAnnotation(Controller.class)){
			throw new ActionException("the class is not a controller.");
		}
		String nameSpace = object.getClass().getAnnotation(Controller.class).value();
		if(StringUtils.isNotBlank(nameSpace)){
			nameSpace = nameSpace.equals("/") ? nameSpace : nameSpace + "/";
		}
		renderFactory.getDefaultRender().render(request, response, nameSpace + view);
	}

	public void renderText(final String text) {
		renderFactory.getTextRender(text).render(request, response, viewPath);
	}

	public void renderText(final String text, final String contentType) {
		renderFactory.getTextRender(text, contentType).render(request, response, viewPath);
	}

	public void renderJS(final String jsText) {
		renderFactory.getJavascriptRender(jsText).render(request, response, viewPath);
	}

	public void renderHtml(final String htmlText) {
		renderFactory.getHtmlRender(htmlText).render(request, response, viewPath);
	}

	public void redirect(final String url) {
		renderFactory.getRedirectRender(url).render(request, response, viewPath);
	}

	public Render getRender() {
		return defaultRender;
	}
	
}

package org.unique.web.exception;

import org.unique.common.tools.StringUtils;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;

/**
 * Action异常类
 * @author biezhi
 * @version 0.1.0
 */
public class ActionException extends RuntimeException{

	private static final long serialVersionUID = 1998063243843477017L;
	private int errorCode;
	private Render errorRender;
	
	public ActionException() {
		throw new IllegalArgumentException("The parameter errorRender can not be null.");
	}
	
	public ActionException(String msg) {
		throw new RuntimeException(msg);
	}
	
	public ActionException(int errorCode, Render errorRender) {
		if (errorRender == null)
			throw new IllegalArgumentException("The parameter errorRender can not be null.");
		this.errorCode = errorCode;
		this.errorRender = errorRender;
	}
	
	public ActionException(int errorCode, String errorView) {
		if (StringUtils.isBlank(errorView)){
			throw new IllegalArgumentException("The parameter errorView can not be blank.");
		}
		this.errorCode = errorCode;
		this.errorRender = RenderFactory.single().getErrorRender(errorCode, errorView);
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public Render getErrorRender() {
		return errorRender;
	}
}

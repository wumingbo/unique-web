package org.unique.web.exception;

/**
 * ActionInvoke异常类
 * @author biezhi
 * @since 1.0
 */
public class ActionInvokeException extends RuntimeException {

	private static final long serialVersionUID = 1998063243843477017L;

	public ActionInvokeException() {
		throw new IllegalArgumentException("argument type mismatch");
	}

	public ActionInvokeException(Exception e) {
		e.printStackTrace();
	}

}

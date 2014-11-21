package org.unique.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler
 * @author biezhi
 * @since 1.0
 */
public abstract class Handler {

	public abstract boolean handle(String target, HttpServletRequest request, HttpServletResponse response);
}

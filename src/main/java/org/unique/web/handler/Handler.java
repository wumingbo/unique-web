package org.unique.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler
 * @author biezhi
 * @version 0.1.0
 */
public interface Handler {

	boolean handle(String target, HttpServletRequest request, HttpServletResponse response);
}

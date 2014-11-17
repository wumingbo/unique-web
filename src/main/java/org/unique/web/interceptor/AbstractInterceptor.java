package org.unique.web.interceptor;

import org.unique.web.core.ActionInvocation;

/**
 * 抽象全局拦截器
 * @author biezhi
 * @version 0.1.0
 */
public abstract class AbstractInterceptor implements Interceptor {
	
	/**
	 * 在action执行前执行
	 * @param ai ActionInvocation对象
	 * @throws Exception
	 */
	public void before(ActionInvocation ai) throws Exception {
	}

	/**
	 * 在action执行后执行
	 * @param ai ActionInvocation对象
	 * @throws Exception
	 */
	public void after(ActionInvocation ai) throws Exception {
	}

	/**
	 * 执行拦截器方法
	 * @param ai ActionInvocation对象
	 * @throws Exception
	 * @return Object 执行结果
	 */
	public abstract Object intercept(ActionInvocation ai) throws Exception;
}

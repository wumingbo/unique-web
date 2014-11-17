package org.unique.web.interceptor;

import org.unique.web.core.ActionInvocation;

/**
 * 拦截器接口
 * @author biezhi
 * @since 1.0
 */
public interface Interceptor {

	/**
	 * 拦截器方法
	 * @param ai ActionInvocation对象
	 * @return 执行结果
	 * @throws Exception
	 */
	Object intercept(ActionInvocation ai) throws Exception;

}

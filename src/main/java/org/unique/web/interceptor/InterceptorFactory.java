package org.unique.web.interceptor;

import java.util.List;

import org.unique.common.tools.CollectionUtil;
import org.unique.web.annotation.Intercept;

/**
 * 拦截器工厂接口
 * @author biezhi
 * @since 1.0
 */
public class InterceptorFactory {

	private static final List<Interceptor> interceptorListTemp = CollectionUtil.newArrayList();
	private static final List<Interceptor> interceptorList = CollectionUtil.newArrayList();

	public static void setInterceptor(Interceptor interceptor){
		interceptorListTemp.add(interceptor);
	}
	
	/**
	 * 设置拦截器
	 * @param interceptor 拦截器class
	 */
	public static void setInterceptor(Class<?> interceptor){
		Object obj = null;
		try {
			obj = interceptor.newInstance();
			if(null != obj && obj instanceof Interceptor){
				interceptorListTemp.add((Interceptor)obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化拦截器
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void buildInterceptor() throws InstantiationException, IllegalAccessException {
		interceptorList.addAll(interceptorListTemp);
		for (Interceptor interceptor : interceptorListTemp) {
			int v = interceptor.getClass().getAnnotation(Intercept.class).loadOnStartup();
			interceptorList.set(v, interceptor);
		}
	}

	/**
	 * 获取所有拦截器
	 * @return 拦截器集合
	 */
	public static List<Interceptor> getInterceptors() {
		return interceptorList;
	}
}

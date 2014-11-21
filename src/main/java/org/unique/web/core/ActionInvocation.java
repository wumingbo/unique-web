package org.unique.web.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.unique.ioc.Container;
import org.unique.ioc.annotation.Autowired;
import org.unique.ioc.impl.DefaultContainerImpl;
import org.unique.web.exception.ActionInvokeException;
import org.unique.web.interceptor.AbstractInterceptor;
import org.unique.web.interceptor.Interceptor;
import org.unique.web.interceptor.InterceptorFactory;

/**
 * ActionInvocation
 * @author biezhi
 * @since 1.0
 */
public class ActionInvocation {

	private final Container container = DefaultContainerImpl.single();

	private String nameSpace;
	
	private Action action;

	private Object[] args;

	private Object result = null;

	private final List<Interceptor> interceptorList = InterceptorFactory.getInterceptors();

	private Integer pos = 0;

	public ActionInvocation(Action action, String nameSpace) {
		super();
		this.action = action;
		this.nameSpace = nameSpace;
		this.args = action.getParameters();
		for (int i = 0; null != args && i < args.length; i++) {
			if (args[i].toString().contains(R.class.getName())) {
				args[i] = new R(ActionContext.getHttpServletRequest(), ActionContext.getHttpServletResponse());
			}
		}
	}

	/**
	 * 执行action
	 * @return 执行结果
	 */
	public Object invoke() {
		try {
			if (interceptorList.size() == 0 || interceptorList.size() == pos) {
				return action.getMethod().invoke(this.newInstance(action.getControllerClass()), args);
			} else {
				Interceptor inter = interceptorList.get(pos);
				pos++;
				if (inter instanceof AbstractInterceptor) {
					AbstractInterceptor ab = (AbstractInterceptor) inter;
					ab.before(this);
					result = ab.intercept(this);
					ab.after(this);
				} else {
					result = inter.intercept(this);
				}
			}
		} catch (IllegalAccessException e) {
			throw new ActionInvokeException(e);
		} catch (IllegalArgumentException e) {
			throw new ActionInvokeException();
		} catch (InvocationTargetException e) {
			throw new ActionInvokeException(e);
		} catch (Exception e) {
			throw new ActionInvokeException(e);
		}
		return result;
	}

	/**
	 * 创建一个新的实例
	 * @param clazz 要被修改的class
	 * @return 修改后的实例
	 */
	private Object newInstance(Class<?> clazz) {
		try {
			Object obj = clazz.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (null != autowired) {
					// 要注入的字段
					Object wiredField = container.getBean(field.getType());
					// 指定装配的类
					if (autowired.value() != Class.class) {
						wiredField = container.getBean(autowired.value());
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					} else {
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					}
					if (null == wiredField) {
						throw new RuntimeException("Unable to load " + field.getType().getCanonicalName() + "！");
					}
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
					field.set(obj, wiredField);
					field.setAccessible(accessible);
				}
			}
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Action getAction() {
		return action;
	}
	
	public String getNameSpace(){
		return nameSpace;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object getResult() {
		return result;
	}

}

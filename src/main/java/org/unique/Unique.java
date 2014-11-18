package org.unique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.unique.ioc.Container;
import org.unique.ioc.impl.BeanFactory;
import org.unique.ioc.impl.DefaultContainerImpl;
import org.unique.support.Support;
import org.unique.support.SupportManager;
import org.unique.tools.ClassHelper;
import org.unique.tools.PropUtil;
import org.unique.web.annotation.Controller;
import org.unique.web.annotation.Intercept;
import org.unique.web.core.Action;
import org.unique.web.core.ActionContext;
import org.unique.web.core.ActionMapping;
import org.unique.web.handler.Handler;
import org.unique.web.handler.impl.DefalutHandlerImpl;
import org.unique.web.interceptor.Interceptor;
import org.unique.web.interceptor.InterceptorFactory;
import org.unique.web.listener.WebInitContextListener;

/**
 * 框架执行流程
 * @author biezhi
 * @since 1.0
 */
public final class Unique {

	private Logger logger = Logger.getLogger(Unique.class);

	private Handler handler;

	private ActionMapping actionMapping;
	
	// ioc容器
	private Container container;

	private Unique() { }
	
	public static Unique single() {
		return SingleHoder.single;
	}

	private static class SingleHoder {

		private static final Unique single = new Unique();
	}

	/**
	 * 初始化方法
	 * @return true:初始化成功 false:初始化失败
	 */
	public boolean init(final String configPath) {

		// 初始化配置文件
		initConst(configPath);

		// 初始化IOC容器
		initIOC();

		// 初始化ActionMapping
		initActionMapping();

		// 初始化拦截器
		initInterceptor();

		// 初始化handler
		initHandler();
		
		// 初始化自定义上下文
		initContext();
		
		// 初始化第三方增强
		initSupport();
		
		return true;
	}

	/**
	 * 初始化常量
	 */
	private void initConst(final String configPath){
		// 加载默认配置文件
		Map<String, String> defaultCfg = PropUtil.getPropertyMap(Const.DEFAULT_CONFIG);
		defaultCfg.putAll(PropUtil.getPropertyMap(configPath));
		Const.putAllConst(defaultCfg);
		defaultCfg = null;
		if(Const.getConfig("unique.encoding").trim().length() > 0){
			Const.ENCODING = Const.getConfig("unique.encoding").trim();
		}
		if(Const.getConfig("unique.view.type").trim().length() > 0){
			Const.RENDER_TYPE = Const.getConfig("unique.view.type").trim();
		}
	}
	/**
	 * 初始化第三方增强
	 */
	private void initSupport() {
		List<Class<?>> supportList = ClassHelper.scanClasses("org.unique.support", Support.class);
		if(supportList.size() > 0){
			try {
				// 第一个启动orm增强
				Class<?> ormClass = Class.forName("org.unique.support.orm.OrmSupport");
				if (supportList.contains(ormClass)) {
					Support support = (Support) ormClass.newInstance();
					support.startup();
					SupportManager.put(ormClass.getName(), support);
					supportList.remove(ormClass);
				}
				for (Class<?> clazz : supportList) {
					Support support = (Support) clazz.newInstance();
					support.startup();
					SupportManager.put(clazz.getName(), support);
				}
			} catch (InstantiationException e) {
				logger.error("初始化增强失败: " + e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("初始化增强失败: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				logger.error("类没有被找到: " + e.getMessage());
			}
		}
	}

	/**
	 * 初始化全局拦截器
	 */
	private void initInterceptor() {
		try {
			InterceptorFactory.buildInterceptor();
		} catch (InstantiationException e) {
			logger.error("初始化拦截器失败: " + e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("初始化拦截器失败: " + e.getMessage());
		}
	}

	/**
	 * 初始化ioc容器
	 */
	private void initIOC() {
		container = DefaultContainerImpl.single();
		BeanFactory.init(container);
		//初始化自定义类
		loadCustomClass();
		logger.info("beans : " + container.getBeanMap());
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		handler = DefalutHandlerImpl.create();
	}
	
	/**
	 * 初始化actionMapping
	 */
	private void initActionMapping() {
		actionMapping = ActionMapping.single();
		Map<String, Action> mapping = actionMapping.buildActionMapping();
		Set<String> matcherSet = mapping.keySet();
		List<String> matcherList = new ArrayList<String>(matcherSet);
		Collections.sort(matcherList);
		for (String r : matcherList) {
			//logger.info("action ：" + r.replace("/(\\d+)", "/@parma"));
			logger.info("action ：" + r);
		}
		logger.info("action size ：" + matcherList.size());
	}
	
	/**
	 * 获取handler
	 * @return Handler对象
	 */
	public Handler getHandler() {
		if(null != this.handler){
			return this.handler;
		}
		return null;
	}

	/**
	 * 扫描加载自定义类
	 */
	private void loadCustomClass() {
		String scanPackage = Const.getConfig("unique.scannpackage");
		if ((scanPackage == null) || (scanPackage.trim().length() == 0)) {
			throw new IllegalArgumentException("扫描的包不能为空!");
		}
		if (scanPackage.indexOf("org.unique") != -1) {
			throw new IllegalArgumentException("要扫描的包名称不能包含\"org.unique\"");
		}
		String[] packages = null;
		if (scanPackage.indexOf(',') == -1) {
			packages = new String[] { scanPackage };
		} else {
			packages = scanPackage.split(",");
		}
		for (String pack : packages) {
			scanPack(pack);
		}
	}

	/**
	 * 初始化Context
	 */
	private void initContext() {
		Object obj = container.getBean(WebInitContextListener.class.getName());
		if (null != obj && obj instanceof WebInitContextListener) {
			WebInitContextListener initListener = (WebInitContextListener) obj;
			initListener.contextInit(ActionContext.getServletContext());
		}
	}

	/**
	 * 扫描包
	 * @param pack 要扫描的包名
	 */
	private void scanPack(String pack) {
		if (pack.endsWith(".*")) {
			pack = pack.substring(0, pack.length() - 2);
		}
		Set<Class<?>> classes = ClassHelper.scanPackage(pack, true);
		for (Class<?> clazz : classes) {
			
			// 添加控制器
			if(null != clazz.getAnnotation(Controller.class)){
				container.putController(clazz);
			}
			
			// 添加拦截器
			if (Interceptor.class.isAssignableFrom(clazz) && 
					null != clazz.getAnnotation(Intercept.class)) {
				InterceptorFactory.setInterceptor(clazz);
			}
				
			if (container.isRegister(clazz.getAnnotations())) {
				container.registBean(clazz);
			}
		}
		// 初始化注入
		container.initWired();
	}

}

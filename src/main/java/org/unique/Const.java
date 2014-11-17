package org.unique;

import java.util.Map;

/**
 * 常量类
 * 
 * @author biezhi
 * @since　1.0
 */
public final class Const {
	
	public static final String UNIQUE_VERSION = "1.0";
	public static final String DEFAULT_CONFIG = "unique-default.properties";
	public static final String CUSTOM_CONFIG = "unique-config.properties";
	public static final String SUPPORT_PACKAGE = "org.unique.support";
	public static final int DEFAULT_PORT = 8080;
	private static Map<String, String> CONFIG_MAP;

	public static String RENDER_TYPE = "jsp";
	public static String ENCODING = "UTF-8";
	
	private Const() {}

	public static void putAllConst(final Map<String, String> configMap){
		CONFIG_MAP = configMap;
	}
	
	/**
	 * 获取配置map
	 * @return 配置map
	 */
	public static Map<String, String> getConfigMap(){
		return CONFIG_MAP;
	}
	
	/**
	 * 根据key获取配置value
	 * @param key 配置文件的key
	 * @return 配置对应的value
	 */
	public static String getConfig(final String key){
		if(CONFIG_MAP.containsKey(key)){
			return CONFIG_MAP.get(key);
		}
		return "";
	}
}

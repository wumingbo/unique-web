package org.unique;

import java.util.Map;

import org.unique.common.tools.CollectionUtil;
import org.unique.common.tools.PropUtil;
import org.unique.common.tools.StringUtils;
import org.unique.web.render.RenderType;

/**
 * 常量类
 * 
 * @author biezhi
 * @version　1.0
 */
public abstract class Const {
	
	public static final String UNIQUE_VERSION = "1.0";
	public static final String DEFAULT_CONFIG = "unique-default.properties";
	public static final String SUPPORT_PACKAGE = "org.unique.support";
	public static final int DEFAULT_PORT = 8080;
	private static final Map<String, String> CONFIG_MAP = CollectionUtil.newHashMap();
	
	public static RenderType RENDER_TYPE = RenderType.JSP;
	public static String ENCODING = "UTF-8";
	
	private Const() {}

	/**
	 * 初始化常量
	 * 
	 * @param configPath 配置文件路径
	 */
	public static void init(final String configPath) {
		// 加载默认配置文件
		Map<String, String> defaultCfg = PropUtil.getPropertyMap(DEFAULT_CONFIG);
		if (StringUtils.isNotBlank(configPath)) {
			defaultCfg.putAll(PropUtil.getPropertyMap(configPath));
		} else {
			defaultCfg.putAll(PropUtil.getPropertyMap("config.properties"));
		}
		CONFIG_MAP.putAll(defaultCfg);
		if(StringUtils.isNotBlank(CONFIG_MAP.get("unique.encoding"))){
			Const.ENCODING = CONFIG_MAP.get("unique.encoding").trim();
		}
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
		return null;
	}
}

package org.unique.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * properties file util
 * @author biezhi
 * @version 0.1.0
 */
public class PropUtil {

	private static final Logger logger = Logger.getLogger(PropUtil.class);

	/**
	 * 根据文件名读取properties文件
	 */
	public static Properties getProperty(String resourceName) {
		InputStream in = null;
		Properties props = new Properties();
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
			if (in != null) {
				props.load(in);
			}
		} catch (IOException e) {
			logger.error("加载属性文件出错！", e);
		} finally {
			IOUtil.closeQuietly(in);
		}
		return props;
	}

	/**
	 * //TODO 根据文件名读取map数据
	 */
	public static Map<String, String> getPropertyMap(String resourceName) {
		InputStream in = null;
		Map<String, String> map = CollectionUtil.newHashMap();
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
			if (null == in) {
				throw new RuntimeException("the properties file [" + resourceName + "] not found!");
			}
			Properties prop = new Properties();
			prop.load(in);
			Set<Entry<Object, Object>> set = prop.entrySet();
			Iterator<Map.Entry<Object, Object>> it = set.iterator();
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				map.put(entry.getKey().toString(), entry.getValue().toString());
			}
			logger.info("Loading properties file from class path resource [" + resourceName + "]");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(in);
		}
		return map;
	}

	/**
	 * //TODO 根据property对象获取map格式数据
	 */
	public static Map<String, String> getPropertyMap(Properties prop) {
		Map<String, String> map = CollectionUtil.newHashMap();
		Set<Entry<Object, Object>> set = prop.entrySet();
		Iterator<Map.Entry<Object, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			map.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return map;
	}
}

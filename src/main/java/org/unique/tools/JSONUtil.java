package org.unique.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * json util
 * @author biezhi
 * @version 0.1.0
 */
@SuppressWarnings("unchecked")
public class JSONUtil {

    public static <T> String toJSON(T t) {
        if (null != t) {
            return JSON.toJSONString(t);
        }
        return null;
    }

    /**
     * map转json
     */
    public static <K, V> String map2Json(Map<K, V> map) {
        if (!CollectionUtil.isEmpty(map)) {
            return JSON.toJSONString(map);
        }
        return null;
    }

    /**
     * list转json
     */
    public static <T> String list2JSON(List<T> list) {
        if (!CollectionUtil.isEmpty(list)) {
            return JSON.toJSONString(list);
        }
        return null;
    }

    /**
     * JSON转map
     */
    public static <K, V> Map<K, V> json2Map(final String json) {
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json, HashMap.class);
        }
        return null;
    }

    /**
     * JSON转list
     */
    public static <T> List<T> json2List(final String json) {
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json, List.class);
        }
        return null;
    }

    /**
     * json转对象
     */
    public static <T> T json2Object(final String json, Class<T> clazz) {
        if (StringUtils.isNotBlank(json) && null != clazz) {
            return JSON.parseObject(json, clazz);
        }
        return null;
    }

}

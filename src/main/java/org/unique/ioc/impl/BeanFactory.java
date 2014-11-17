package org.unique.ioc.impl;

import java.util.Set;

import org.unique.ioc.BeanType;
import org.unique.ioc.Container;

/**
 * Bean工厂
 * @author biezhi
 * @version 0.1.0
 */
@SuppressWarnings("unchecked")
public class BeanFactory {
    
    private static Container container;
    
    public static void init(Container container_) {
        container = container_;
    }
    
    /**
     * 根据类名获取bean实例
     * @param name 类全名称
     * @param beanType 获取bean的方式
     * @return Object
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static Object getBean(String name, BeanType beanType) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (null == beanType) {
            return container.getBean(name);
        }
        if (beanType == BeanType.SINGLE) {
            return container.getBean(name);
        }
        return Class.forName(name).newInstance();
    }

    /**
     * 根据class获取bean实例
     * @param type Bean的class类型
     * @param beanType 获取bean的方式
     * @return Object
     * @throws Exception
     */
    public static Object getBean(Class<?> type, BeanType beanType) throws Exception {
        if (null == beanType) return container.getBean(type);
        if (beanType == BeanType.SINGLE) return container.getBean(type);
        return type.newInstance();
    }

    /**
     * 获取容器所有对象名称
     * @return 返回所有bean的名称
     */
    public static Set<String> getBeanNames() {
        return (Set<String>) container.getBeanNames();
    }

    /**
     * 获取容器所有对象
     * @return 返回所有bean对象
     */
    public static Object[] getBeans() {
        return container.getBeans().toArray();
    }

}

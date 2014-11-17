package org.unique.ioc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 容器顶层接口
 * @author biezhi
 * @version 0.1.0
 */
public interface Container {

    Object getBean(String name);

    Object getBean(Class<?> type);

    Set<?> getBeanNames();
    
    List<Class<?>> getControllers();
    
    void putController(Class<?> clazz);
    
    Collection<?> getBeans();

    boolean hasBean(Class<?> clazz);

    boolean hasBean(String name);

    Object registBean(Class<?> clazz);

    void initWired();

    boolean isRegister(Annotation[] annotations);
    
    Map<String, Object> getBeanMap();
}

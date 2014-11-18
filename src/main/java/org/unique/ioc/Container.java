package org.unique.ioc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 容器顶层接口
 * @author biezhi
 * @since 1.0
 */
public interface Container {

    Object getBean(String name);

    Object getBean(Class<?> type);

    Set<?> getBeanNames();
    
    List<Class<?>> getControllers();
    
    Collection<?> getBeans();

    boolean hasBean(Class<?> clazz);

    boolean hasBean(String name);
    
    boolean isRegister(Annotation[] annotations);

    Object registBean(Class<?> clazz);

    void initWired();

    void putController(Class<?> clazz);
    
    Map<String, Object> getBeanMap();
}

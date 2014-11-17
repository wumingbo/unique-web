package org.unique.common.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * class scanner
 * @author biezhi
 * @version 0.1.0
 */
public class ClassHelper {

    private static final Logger logger = Logger.getLogger(ClassHelper.class);

    /**
     * 扫描包
     * @param pack 包名
     * @param recursive 是否包含子包
     * @return
     */
    public static Set<Class<?>> scanPackage(final String pack, final boolean recursive) {
        Set<Class<?>> classes = CollectionUtil.newHashSet();
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的URL
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                ClassHelper.findClassesInPackageByFile(packageName, filePath, recursive, classes);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     */
    private static void findClassesInPackageByFile(final String packageName, final String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if ((!dir.exists()) || (!dir.isDirectory())) {
            logger.warn("user defined " + packageName + " not a file !");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {

            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    logger.error("add user custom view class error, can't find such .class file !");
                }
            }
        }
    }
    
    /**
     * 根据包名和父类型扫描
     * @param packageName 要扫描的包名称
     * @param parent
     * @return
     */
    public static List<Class<?>> scanClasses(final String packageName, final Class<?> parent){
    	Set<Class<?>> classSet = scanPackage(packageName, true);
    	List<Class<?>> classList = CollectionUtil.newArrayList(classSet.size());
    	for(Class<?> clazz : classSet){
    		if(clazz.getSuperclass().equals(parent)){
    			classList.add(clazz);
    		}
    	}
    	return classList;
    }
   
}

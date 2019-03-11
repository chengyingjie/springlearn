package com.spring.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ClassLoaderUtil {

    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String POSTFIX = ".class";

    /**
     * 根据包名获取获取Class
     */
    public static Set<Class<?>> getClasses(String packageName) {

        if (packageName == null || "".equals(packageName)) {
            return Collections.emptySet();
        }

        //将包名改为相对路径
        String packagePath = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classes = new HashSet<>();

        try {
            //扫描包路径，返回资源的枚举
            Enumeration<URL> dirs = classLoader.getResources(packagePath);
            while (dirs.hasMoreElements()) {
                URL fileUrl = dirs.nextElement();
                String filePath = fileUrl.getPath();
                // 根据资源类型记载对应class文件
                if (FILE_PROTOCOL.equals(fileUrl.getProtocol())) {
                    //处理文件类型的Class
                    classes.addAll(getClassesByFilePath(filePath, packagePath));
                } else if (JAR_PROTOCOL.equals(fileUrl.getProtocol())) {
                    //处理Jar包中的Class
                    JarURLConnection jarURLConnection = (JarURLConnection) fileUrl.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    classes.addAll(getClassesByJar(jarFile));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static Set<Class<?>> getClassesByFilePath(String filePath, String packagePath) {

        Set<Class<?>> classes = new HashSet<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null) {
            return classes;
        }

        for (File childFile : childFiles) {
            String path = childFile.getAbsolutePath();
            if (!childFile.isDirectory() && path.endsWith(POSTFIX)) {
                String className = path.substring(path.indexOf(packagePath), path.lastIndexOf(POSTFIX)).replaceAll("/", ".");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                classes.addAll(getClassesByFilePath(path, packagePath));
            }
        }
        return classes;
    }

    private static Set<Class<?>> getClassesByJar(JarFile jarFile) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String entryName = jarEntry.getName();
                if (!jarEntry.isDirectory() && entryName.endsWith(POSTFIX)) {
                    String className = entryName.substring(0, entryName.lastIndexOf(POSTFIX)).replaceAll("/", ".");
                    Class clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }

}

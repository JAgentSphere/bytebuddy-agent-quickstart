package com.jas.quickstart.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class ClassScanner {
    public static Set<Class<?>> getWithAnnotation(ClassLoader classLoader, String packageName,
        Class<? extends Annotation> annotationClass) throws Exception {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        List<JarFile> jars = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".endsWith(resource.getProtocol())) {
                dirs.add(new File(resource.getFile()));
            }
            if ("jar".equals(resource.getProtocol())) {
                jars.add(((JarURLConnection)resource.openConnection()).getJarFile());
            }
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(findClassesFromFile(directory, packageName, annotationClass));
        }
        for (JarFile jar : jars) {
            classes.addAll(findClassesFromJar(jar, packageName, annotationClass));
        }
        return classes;
    }

    private static List<Class<?>> findClassesFromFile(File directory, String packageName,
        Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (Objects.isNull(files)) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesFromFile(file, packageName + "." + file.getName(), annotationClass));
            } else if (file.getName().endsWith(".class")) {
                Class<?> clazz =
                    Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (clazz.isAnnotationPresent(annotationClass)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> findClassesFromJar(JarFile jarFile, String packageName,
        Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        Enumeration<JarEntry> jarIterator = jarFile.entries();
        List<Class<?>> classes = new ArrayList<>();
        while (jarIterator.hasMoreElements()) {
            JarEntry entry = jarIterator.nextElement();
            String name = entry.getName();
            if (name.startsWith(packageName.replace('.', '/')) && name.endsWith(".class") && !entry.isDirectory()) {
                String className = name.substring(0, name.length() - 6).replace("/", ".");
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(annotationClass)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
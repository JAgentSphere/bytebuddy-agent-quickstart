package com.jas.quickstart.core.utils;

import com.jas.quickstart.core.plugins.Plugin;
import org.junit.jupiter.api.Test;

import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author ReaJason
 * @since 2024/2/1
 */
class PluginScannerTest {
    @Test
    public void testPlugin() throws Exception {
        Set<Class<?>> classesWithAnnotation = ClassScanner.getWithAnnotation(
            PluginScannerTest.class.getClassLoader(), "com.jas.quickstart.core.plugins", Plugin.class);
        assertEquals(classesWithAnnotation.size(), 3);
    }

    @Test
    public void testJar() throws Exception{
        JarFile jarFile = new JarFile("/Users/reajason/IdeaProjects/bytebuddy-agent-quickstart/core/build/libs/core.jar");
        Enumeration<JarEntry> jarIterator =
                jarFile.entries();
        while (jarIterator.hasMoreElements()) {
            JarEntry entry = jarIterator.nextElement();
            String name = entry.getName();
            if(name.startsWith("com.jas.quickstart.core.plugins") && name.endsWith(".class") && !entry.isDirectory()){
                String className = name.substring(0, name.length() - 6).replace("/", ".");
            }
        }
    }

}
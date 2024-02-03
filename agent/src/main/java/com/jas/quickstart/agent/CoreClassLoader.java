package com.jas.quickstart.agent;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public class CoreClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public CoreClassLoader(URL[] urls) {
        super(urls, getSystemClassLoader().getParent());
    }
}

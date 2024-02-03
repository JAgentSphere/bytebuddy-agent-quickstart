package com.jas.quickstart.core.aop.interceptor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.io.IOException;

public class DumpListener implements AgentBuilder.Listener {

        @Override
        public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            try {
                dynamicType.saveIn(new File("./build/weaving"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }
    }
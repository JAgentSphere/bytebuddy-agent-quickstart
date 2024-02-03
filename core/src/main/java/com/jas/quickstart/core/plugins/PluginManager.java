package com.jas.quickstart.core.plugins;

import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.utils.ClassScanner;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author ReaJason
 * @since 2024/2/1
 */
@Slf4j
public class PluginManager {

    private final String PLUGIN_PACKAGE_NAME = PluginManager.class.getPackage().getName();

    @Getter
    private final List<Aspect> plugins = new ArrayList<>();

    public void init() {
        try {
            log.debug("starting scan plugins in {}", PLUGIN_PACKAGE_NAME);
            Set<Class<?>> classes =
                ClassScanner.getWithAnnotation(PluginManager.class.getClassLoader(), PLUGIN_PACKAGE_NAME, Plugin.class);
            log.debug("current context has {} plugins", classes.size());
            for (Class<?> clazz : classes) {
                if (!Aspect.class.isAssignableFrom(clazz)) {
                    log.error("invalid annotation of [@Plugin], must be on a class that implements the [Aspect] "
                        + "interface");
                    continue;
                }
                try {
                    Aspect aspect = (Aspect)clazz.newInstance();
                    log.debug("plugin [{}] instantiated success", clazz.getName());
                    plugins.add(aspect);
                } catch (Throwable t) {
                    log.error("plugin [{}] could not be instantiated.", clazz.getName(), t);
                }
            }

        } catch (Exception e) {
            // ignore
        }
    }

    public void destroy(Instrumentation instrumentation) {
        for (Aspect aspect : plugins) {
            List<ClassFileTransformer> transformers = aspect.getTransformers();
            for (ClassFileTransformer transformer : transformers) {
                boolean rmResult = Objects.requireNonNull(instrumentation).removeTransformer(transformer);
                if (!rmResult) {
                    log.error("remove transformer failed: {}", transformer.getClass().getName());
                } else {
                    List<String> methodSignatures = aspect.getMethodSignatures();
                    for (String methodSignature : methodSignatures) {
                        log.info("remove transformer success: {}", methodSignature);
                    }
                }
            }
        }
    }
}

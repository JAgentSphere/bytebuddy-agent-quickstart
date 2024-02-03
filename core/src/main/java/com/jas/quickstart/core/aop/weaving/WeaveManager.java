package com.jas.quickstart.core.aop.weaving;

import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.aop.Pointcut;
import com.jas.quickstart.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.none;

/**
 * 负责将 advice 注入到目标类中
 *
 * @author ReaJason
 * @since 2024/1/28
 */
@Slf4j
public class WeaveManager {

    private AgentBuilder agentBuilder;

    private AgentBuilder nativeAgentBuilder;

    public Map<Integer, Aspect> weave(Instrumentation instrumentation, List<Aspect> aspects) {
        Map<Integer, Aspect> aspectMap = new HashMap<>(aspects.size());
        for (Aspect aspect : aspects) {
            Pointcut pointcut = aspect.getPointcut();
            List<ClassFileTransformer> transformers = new ArrayList<>();
            if (pointcut.isNative()) {
                log.info("native method found, use rebase strategy");

                // just rebase native method, don't inject advice, because native transformer cannot be uninstalled
                transformers.add(nativeAgentBuilder.type(pointcut.getTypeMatcher())
                        .transform(new NativeRebaseTransformer(aspect.getPointcut().getMethodMatcher()))
                        .installOn(instrumentation));
            }

            // either native or not, we need to inject advice, this transformer can be uninstalled
            transformers.add((agentBuilder.type(pointcut.getTypeMatcher())
                    .transform(new AdviceTransformer(aspect, aspectMap)).installOn(instrumentation)));

            aspect.setTransformers(transformers);
        }

        return aspectMap;
    }

    public void init(boolean dumpClass, String dumpFolder) {
        agentBuilder = new AgentBuilder.Default().disableClassFormatChanges()
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                .ignore(ElementMatchers.nameStartsWith("com.jas.quickstart.core."))
                .ignore(ElementMatchers.nameStartsWith("java.arthas."))
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new CustomTransformListener(dumpClass, dumpFolder));

        String NATIVE_METHOD_PREFIX = "jas$$";
        nativeAgentBuilder = new AgentBuilder.Default().ignore(none()).enableNativeMethodPrefix(NATIVE_METHOD_PREFIX)
                .with(AgentBuilder.TypeStrategy.Default.REBASE).with(new ErrorTransformListener());
    }

    @Slf4j
    private static class ErrorTransformListener implements AgentBuilder.Listener {

        @Override
        public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                     boolean loaded, DynamicType dynamicType) {

        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                              boolean loaded) {

        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
                            Throwable throwable) {
            log.error("transform class: {} failed", typeName, throwable);
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }
    }

    @Slf4j
    private static class CustomTransformListener extends ErrorTransformListener {
        private final boolean dumpClass;
        private File dumpFolder;

        public CustomTransformListener(boolean dumpClass, String dumpFolder) {
            this.dumpClass = dumpClass;

            if (dumpClass && StringUtil.isNotEmpty(dumpFolder)) {
                Path path = Paths.get(dumpFolder);
                if (Files.notExists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        log.error("dumpFolder create error, please check the path is valid", e);
                    }
                }
                this.dumpFolder = path.toFile();
            }
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                     boolean loaded, DynamicType dynamicType) {
            log.info("transform class: {} success", typeDescription.getName());
            if (dumpClass) {
                try {
                    dynamicType.saveIn(dumpFolder);
                } catch (IOException e) {
                    log.info("dump class error", e);
                }
            }
        }
    }
}

package com.jas.quickstart.core.aop.weaving;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

/**
 * <a href="https://github.com/raphw/byte-buddy/issues/464 ">bytebuddy native hook only work in premain</a>*
 * 
 * @author ReaJason
 * @since 2024/1/28
 */
public class NativeRebaseTransformer implements AgentBuilder.Transformer {

    private final ElementMatcher<MethodDescription> methodMatcher;

    public NativeRebaseTransformer(ElementMatcher<MethodDescription> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
        ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return builder.method(methodMatcher).intercept(Advice.to(NativeInterceptor.class));
    }

    // just rebase native method with empty interceptor
    private static class NativeInterceptor {
        @Advice.OnMethodEnter
        public static void interceptor() {

        }
    }
}

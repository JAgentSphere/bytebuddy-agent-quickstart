package com.jas.quickstart.core.aop.support.interceptor;

import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.spy.Spy;

import static net.bytebuddy.asm.Advice.*;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class ConstructorBefore {

    @OnMethodEnter
    public static void before(@AllArguments Object[] allArguments, @Origin("#t") String className, @AspectId Integer aspectId) {
        Spy.before(aspectId, className, "<init>", allArguments, null);
    }
}

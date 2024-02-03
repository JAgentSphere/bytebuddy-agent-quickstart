package com.jas.quickstart.core.aop;

import com.jas.quickstart.core.DefaultSpyHandler;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.reflect.Executable;

/**
 * @author ReaJason
 * @since 2024/1/25
 */
public interface Pointcut {

    /**
     * 方法匹配器，如果是 Native 方法，请勿添加 isNative 匹配条件，否则在 rebase 后，执行切面方法时不是 native 方法而导致会无法命中
     *
     * @return matcher
     * @see DefaultSpyHandler#getAspectByTypeAndMethod(Class, Executable) 在 method 匹配是 isNative 会出现问题
     */
    ElementMatcher.Junction<MethodDescription> getMethodMatcher();

    /**
     * 类匹配器
     *
     * @return matcher
     */
    ElementMatcher.Junction<TypeDescription> getTypeMatcher();

    default boolean isStatic() {
        return false;
    }

    default boolean isConstructor() {
        return false;
    }

    default boolean isNative() {
        return false;
    }
}

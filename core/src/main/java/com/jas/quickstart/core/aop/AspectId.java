package com.jas.quickstart.core.aop;

import java.lang.annotation.*;

/**
 * @author ReaJason
 * @since 2024/1/28
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectId {
}

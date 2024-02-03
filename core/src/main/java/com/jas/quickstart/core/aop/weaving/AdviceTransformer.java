package com.jas.quickstart.core.aop.weaving;

import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.aop.AspectId;
import com.jas.quickstart.core.aop.Pointcut;
import com.jas.quickstart.core.aop.support.interceptor.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ReaJason
 * @since 2024/1/28
 */
@Slf4j
public class AdviceTransformer implements AgentBuilder.Transformer {
    private final Aspect aspect;
    private final Map<Integer, Aspect> aspectMap;

    public AdviceTransformer(Aspect aspect, Map<Integer, Aspect> aspectMap) {
        this.aspect = aspect;
        this.aspectMap = aspectMap;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
        ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        Pointcut pointcut = aspect.getPointcut();

        // get pointcut matched method list
        List<MethodDescription.InDefinedShape> methodDes = typeDescription.getDeclaredMethods().stream()
            .filter(pointcut.getMethodMatcher()::matches).collect(Collectors.toList());

        if (methodDes.isEmpty()) {
            return builder;
        }

        int aspectId = 0;
        for (MethodDescription.InDefinedShape methodDe : methodDes) {
            aspectId += methodDes.toString().hashCode();

            List<String> methodSignatures = aspect.getMethodSignatures();
            if (Objects.isNull(methodSignatures)) {
                methodSignatures = new ArrayList<>();
                methodSignatures.add(methodDe.toString());
                aspect.setMethodSignatures(methodSignatures);
            } else {
                methodSignatures.add(methodDe.toString());
            }
        }

        if (aspectId == 0) {
            throw new RuntimeException("current aspectId is 0, the method is " + typeDescription.getName());
        }

        aspectMap.put(aspectId, aspect);

        if (aspect.isAround()) {
            if (pointcut.isStatic()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(StaticMethodBefore.class, StaticMethodAfter.class).on(pointcut.getMethodMatcher()));
            }

            if (pointcut.isConstructor()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(ConstructorBefore.class, ConstructorAfter.class).on(pointcut.getMethodMatcher()));
            }

            return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                .to(InstMethodBefore.class, InstMethodAfter.class).on(pointcut.getMethodMatcher()));
        }

        if (aspect.isBefore()) {
            if (pointcut.isStatic()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(StaticMethodBefore.class).on(pointcut.getMethodMatcher()));
            }

            if (pointcut.isConstructor()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(ConstructorBefore.class).on(pointcut.getMethodMatcher()));
            }

            return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId).to(InstMethodBefore.class)
                .on(pointcut.getMethodMatcher()));
        }

        if (aspect.isAfter()) {
            if (pointcut.isStatic()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(StaticMethodAfter.class).on(pointcut.getMethodMatcher()));
            }

            if (pointcut.isConstructor()) {
                return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId)
                    .to(ConstructorAfter.class).on(pointcut.getMethodMatcher()));
            }

            return builder.visit(Advice.withCustomMapping().bind(AspectId.class, aspectId).to(InstMethodAfter.class)
                .on(pointcut.getMethodMatcher()));
        }

        throw new RuntimeException("Unsupported aspect type");
    }
}

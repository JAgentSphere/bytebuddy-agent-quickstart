package com.jas.quickstart.core.aop.interceptor;

import com.jas.quickstart.core.DefaultSpyHandler;
import com.jas.quickstart.core.aop.support.interceptor.StaticMethodAfter;
import com.jas.quickstart.core.aop.support.interceptor.StaticMethodBefore;
import com.jas.quickstart.spy.Spy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.none;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class StaticMethodInterceptorTest {
    private ClassLoader classLoader;

    private AgentBuilder agentBuilder;

    @BeforeEach
    void setUp() throws Exception {
        classLoader = new ByteArrayClassLoader.ChildFirst(getClass().getClassLoader(),
                ClassFileLocator.ForClassLoader.readToNames(StaticMethodBeforeFoo.class, StaticMethodAfterFoo.class, StaticMethodAroundFoo.class),
                ByteArrayClassLoader.PersistenceHandler.MANIFEST);
        Spy.setSpyHandler(new DefaultSpyHandler());
        agentBuilder = new AgentBuilder.Default()
                .ignore(none())
                .disableClassFormatChanges().with(new DumpListener());
    }

    @Test
    void testBefore() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(
                        ElementMatchers.is(StaticMethodBeforeFoo.class), ElementMatchers.is(classLoader)
                ).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(StaticMethodBefore.class).on(named("hello"))))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(StaticMethodBeforeFoo.class.getName());
            Object foo = type.newInstance();
            assertEquals(type.getMethod("hello", String.class).invoke(foo, "hello"), "hello");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }


    @Test
    void testAfter() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(
                        ElementMatchers.is(StaticMethodAfterFoo.class), ElementMatchers.is(classLoader)
                ).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(StaticMethodAfter.class).on(named("hello"))))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(StaticMethodAfterFoo.class.getName());
            Object foo = type.newInstance();
            assertEquals(type.getMethod("hello", String.class).invoke(foo, "hello"), "hello");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }


    @Test
    void testAround() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(
                        ElementMatchers.is(StaticMethodAroundFoo.class), ElementMatchers.is(classLoader)
                ).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(StaticMethodBefore.class, StaticMethodAfter.class).on(named("hello"))))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(StaticMethodAroundFoo.class.getName());
            Object foo = type.newInstance();
            assertEquals(type.getMethod("hello", String.class).invoke(foo, "hello"), "hello");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }
}

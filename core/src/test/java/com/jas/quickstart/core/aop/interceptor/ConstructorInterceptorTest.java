package com.jas.quickstart.core.aop.interceptor;

import com.jas.quickstart.core.DefaultSpyHandler;
import com.jas.quickstart.core.aop.support.interceptor.ConstructorAfter;
import com.jas.quickstart.core.aop.support.interceptor.ConstructorBefore;
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
import java.lang.reflect.Field;

import static net.bytebuddy.matcher.ElementMatchers.none;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
class ConstructorInterceptorTest {

    private ClassLoader classLoader;

    private AgentBuilder agentBuilder;

    @BeforeEach
    public void setUp() throws Exception {
        classLoader = new ByteArrayClassLoader.ChildFirst(getClass().getClassLoader(),
                ClassFileLocator.ForClassLoader.readToNames(ConstructorBeforeFoo.class, ConstructorAfterFoo.class, ConstructorAroundFoo.class),
                ByteArrayClassLoader.PersistenceHandler.MANIFEST);
        Spy.setSpyHandler(new DefaultSpyHandler());
        agentBuilder = new AgentBuilder.Default()
                .ignore(none())
                .disableClassFormatChanges().with(new DumpListener());
    }


    @Test
    void testBefore() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(ElementMatchers.is(ConstructorBeforeFoo.class), ElementMatchers.is(classLoader)).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(ConstructorBefore.class).on(ElementMatchers.isConstructor())))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(ConstructorBeforeFoo.class.getName());
            Object foo = type.getConstructor(String.class).newInstance("before");
            Field name = type.getDeclaredField("name");
            name.setAccessible(true);
            assertEquals("before", name.get(foo));

            Object o = type.newInstance();
            assertNull(name.get(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }

    @Test
    void testAfter() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(ElementMatchers.is(ConstructorAfterFoo.class), ElementMatchers.is(classLoader)).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(ConstructorAfter.class).on(ElementMatchers.isConstructor())))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(ConstructorAfterFoo.class.getName());
            Object foo = type.getConstructor(String.class).newInstance("after");
            Field name = type.getDeclaredField("name");
            name.setAccessible(true);
            assertEquals("after", name.get(foo));

            Object o = type.newInstance();
            assertNull(name.get(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }

    @Test
    void testAround() {
        assertInstanceOf(Instrumentation.class, ByteBuddyAgent.install());
        ClassFileTransformer classFileTransformer = agentBuilder.type(ElementMatchers.is(ConstructorAroundFoo.class), ElementMatchers.is(classLoader)).transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(ConstructorBefore.class, ConstructorAfter.class).on(ElementMatchers.isConstructor())))
                .installOnByteBuddyAgent();
        try {
            Class<?> type = classLoader.loadClass(ConstructorAroundFoo.class.getName());
            Object foo = type.getConstructor(String.class).newInstance("around");
            Field name = type.getDeclaredField("name");
            name.setAccessible(true);
            assertEquals("around", name.get(foo));

            Object o = type.newInstance();
            assertNull(name.get(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assertTrue(ByteBuddyAgent.getInstrumentation().removeTransformer(classFileTransformer));
        }
    }


}
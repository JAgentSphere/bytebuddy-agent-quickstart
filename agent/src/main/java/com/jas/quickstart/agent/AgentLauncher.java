package com.jas.quickstart.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author ReaJason
 * @since 2024/1/22
 */
public class AgentLauncher {

    public static final String SPY_CLASS_NAME = "com.jas.quickstart.spy.Spy";
    public static final String SPY_JAR_FILE_NAME = "spy.jar";
    public static final String CORE_JAR_FILE_NAME = "core.jar";
    public static final String CORE_CLASS_NAME = "com.jas.quickstart.core.AgentMain";
    public static final String CORE_INSTALL_METHOD_NAME = "install";
    public static final String CORE_UNINSTALL_METHOD_NAME = "uninstall";
    public static final String BANNER_LOCATION = "/com/jas/quickstart/agent/banner.txt";
    private static Instrumentation instrumentation;
    private static CoreClassLoader coreClassLoader;
    private static Object mainInstance;
    private static final String agentJarFolderPath;

    private static boolean installed = false;

    static {
        String agentJarPath = AgentLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        agentJarFolderPath = new File(agentJarPath).getParent();
    }

    public static void premain(String args, Instrumentation inst) throws Throwable {
        launch(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) throws Throwable {
        launch(args, inst);
    }

    public static void launch(String args, Instrumentation inst) throws Throwable {
        System.out.println(getBanner());
        System.out.println(getAppInfo());

        if (instrumentation == null) {
            instrumentation = inst;
        }

        // install
        if (args == null || CORE_INSTALL_METHOD_NAME.equals(args)) {
            install(args);
        }

        // uninstall
        if (CORE_UNINSTALL_METHOD_NAME.equals(args)) {
            uninstall(args);
        }
    }

    public synchronized static void install(String args) throws Throwable {
        if (installed) {
            uninstall(args);
        }
        addSpyJarToBootstrapClassLoader();
        launchCore();

        installed = true;
        System.out.println("JAgentSphere installed.");
    }

    private static void addSpyJarToBootstrapClassLoader() throws Throwable {
        try {
            Class.forName(SPY_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            String spyJarPath = agentJarFolderPath + File.separator + SPY_JAR_FILE_NAME;
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(spyJarPath));
        }
    }

    private static void launchCore() throws Throwable {
        String coreJarPath = agentJarFolderPath + File.separator + CORE_JAR_FILE_NAME;
        coreClassLoader = new CoreClassLoader(new URL[] {new File(coreJarPath).toURI().toURL()});

        Class<?> mainClass = coreClassLoader.loadClass(CORE_CLASS_NAME);
        mainInstance = mainClass.newInstance();
        Method installMethod = mainClass.getMethod(CORE_INSTALL_METHOD_NAME, Instrumentation.class);
        installMethod.invoke(mainInstance, instrumentation);
    }

    public static void uninstall(String args) throws Throwable {
        if (coreClassLoader == null) {
            System.out.println("JAgentSphere is not installed.");
            return;
        }
        coreClassLoader.loadClass(CORE_CLASS_NAME).getMethod(CORE_UNINSTALL_METHOD_NAME, Instrumentation.class)
            .invoke(mainInstance, instrumentation);
        mainInstance = null;
        coreClassLoader.close();
        coreClassLoader = null;
        installed = false;

        if (CORE_UNINSTALL_METHOD_NAME.equals(args) && Boolean.parseBoolean(System.getProperty("JAgentSphere.debug"))) {
            // call full gc if you want rm all objects right now
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    System.gc();
                } catch (InterruptedException e) {
                    // ignored
                }
                List<Class> collect = Arrays.stream(instrumentation.getAllLoadedClasses())
                    .filter(clazz -> clazz.getClassLoader() != null
                        && clazz.getClassLoader().getClass().getName()
                            .equals("com.jas.quickstart.agent.CoreClassLoader")
                        && clazz.getName().startsWith("com.jas.quickstart."))
                    .collect(Collectors.toList());
                if (collect.size() > 2) {
                    System.out.println("JAgentSphere core existed class size: " + collect.size());
                    System.err.println("JAgentSphere contains memory leak,  objects: ");
                    collect.stream().map(Class::getName).forEach(System.err::println);
                } else {
                    System.out.println(
                        "\uD83C\uDF89 Congratulations \uD83C\uDF89 \nJAgentSphere was uninstalled with no memory leak.");
                }

            }).start();
        }
        System.out.println("JAgentSphere uninstalled.");
    }

    public static String getBanner() {
        try (InputStream is = AgentLauncher.class.getResourceAsStream(BANNER_LOCATION)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception ignore) {
        }
        return "";
    }

    public static String getAppInfo() {
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        String startUser = System.getProperty("user.name");

        String hostname = getHostnameOrEmpty();

        return String.format(
            "  PID: %s\n" + "  Java version: %s\n" + "  Start By: %s\n" + "  Start Path: %s\n" + "  Host: %s %s %s\n",
            pid, javaVersion, startUser, agentJarFolderPath, hostname, osName, osArch);
    }

    private static String getHostnameOrEmpty() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            // ignore
        }
        return "";
    }
}

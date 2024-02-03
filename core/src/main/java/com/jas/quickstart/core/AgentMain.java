package com.jas.quickstart.core;

import com.jas.quickstart.core.aop.Aspect;
import com.jas.quickstart.core.aop.weaving.WeaveManager;
import com.jas.quickstart.core.config.AgentConfig;
import com.jas.quickstart.core.config.ConfigManager;
import com.jas.quickstart.core.logging.LoggerManager;
import com.jas.quickstart.core.plugins.PluginManager;
import com.jas.quickstart.spy.Spy;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ReaJason
 * @since 2024/1/24
 */
@Slf4j
public class AgentMain {

    private LoggerManager loggerManager = new LoggerManager();
    private PluginManager pluginManager = new PluginManager();

    public void install(Instrumentation inst) {
        ConfigManager configManager = new ConfigManager();
        AgentConfig agentConfig = configManager.getConfig();

        // init logging
        loggerManager.init(agentConfig.isDebug());

        // get all aspects
        pluginManager.init();
        List<Aspect> aspects = pluginManager.getPlugins();

        // weave all aspects
        WeaveManager weaveManager = new WeaveManager();
        weaveManager.init(agentConfig.isDumpClass(), agentConfig.getDumpFolder());
        Map<Integer, Aspect> aspectMap = weaveManager.weave(inst, aspects);

        // init spy handler
        DefaultSpyHandler defaultSpyHandler = new DefaultSpyHandler(aspectMap);
        Spy.setSpyHandler(defaultSpyHandler);
        log.info("Installed JAgentSphere.");
    }

    public void uninstall(Instrumentation inst) {
        log.info("Uninstalling JAgentSphere...");

        Spy.clean();

        if (Objects.nonNull(pluginManager)) {
            pluginManager.destroy(inst);
            pluginManager = null;
        }
        log.info("Uninstalled JAgentSphere.");
        if (Objects.nonNull(loggerManager)) {
            loggerManager.destroy();
            loggerManager = null;
        }
    }
}

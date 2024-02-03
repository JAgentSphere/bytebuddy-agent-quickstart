package com.jas.quickstart.core.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.jas.quickstart.core.exception.InitializeException;
import lombok.Data;
import org.slf4j.LoggerFactory;

/**
 * @author ReaJason
 * @since 2024/1/29
 */
@Data
public class LoggerManager {
    private final String PROD_XML_PATH = "logback-prod.xml";
    private final String DEBUG_XML_PATH = "logback-debug.xml";

    /**
     * init logback directly <a href="https://logback.qos.ch/manual/configuration.html#joranDirectly">configuration</a>
     */
    public void init(boolean isDebug) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(context);
        context.reset();
        String xmlFilePath = PROD_XML_PATH;
        if (isDebug) {
            xmlFilePath = DEBUG_XML_PATH;
        }
        try {
            joranConfigurator.doConfigure(LoggerManager.class.getClassLoader().getResourceAsStream(xmlFilePath));
        } catch (JoranException e) {
            throw new InitializeException("logback init error", e);
        }
    }

    public void destroy() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.stop();
    }
}

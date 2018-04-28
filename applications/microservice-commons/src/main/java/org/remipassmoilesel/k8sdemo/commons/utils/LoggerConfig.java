package org.remipassmoilesel.k8sdemo.commons.utils;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

public class LoggerConfig {

    public static void configureLog() {
        configureLog(Level.ERROR);
    }

    public static void configureLog(Level level) {

        // see http://www.tinylog.org/configuration

        Configurator.currentConfig()
                .writer(new ConsoleWriter(), level)
                .formatPattern("{{level}:|min-size=8} {class_name}.{method} {message}")
                .activate();

        Logger.info("Log configured !");

    }

}

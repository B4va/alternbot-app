package config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import utils.EnvironmentVariablesUtils;

import java.net.URI;

/**
 * Configuration du {@link Logger}.
 */
@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfiguration extends ConfigurationFactory {

  private static final String DEFAULT_LEVEL = "INFO";
  private static final String APP_APPENDER = "app";
  private static final String EXT_APPENDER = "ext";
  private static final String APP_PATTERN = "%d{dd-MM-yyyy - HH:mm:ss} [%t] %level | %logger{36} | %msg%n";
  public static final String EXT_PATTERN = "%10level | %logger{36} - %msg%n";

  private static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
    final String level = EnvironmentVariablesUtils
      .getString(EnvironmentVariablesUtils.LOG_LEVEL, DEFAULT_LEVEL).toUpperCase();
    final String pattern = EnvironmentVariablesUtils
      .getString(EnvironmentVariablesUtils.LOG_FORMAT, APP_PATTERN);
    builder.setConfigurationName(name);
    builder.setStatusLevel(Level.ERROR);
    setAppLogs(builder, level, pattern);
    setExternalLogs(builder, level);
    return builder.build();
  }

  private static void setAppLogs(ConfigurationBuilder<BuiltConfiguration> builder, String level, String pattern) {
    AppenderComponentBuilder console = builder.newAppender(APP_APPENDER, "console").
      addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
    console.add(builder.newLayout("PatternLayout")
      .addAttribute("pattern", pattern));
    builder.add(console)
      .add(builder.newRootLogger(Level.getLevel(level))
        .add(builder.newAppenderRef(APP_APPENDER)));
  }

  private static void setExternalLogs(ConfigurationBuilder<BuiltConfiguration> builder, String level) {
    AppenderComponentBuilder console = builder.newAppender(EXT_APPENDER, "console").
      addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
    console.add(builder.newLayout("PatternLayout")
      .addAttribute("pattern", EXT_PATTERN));
    setExternalLoggers(builder, level, console);
  }

  private static void setExternalLoggers(ConfigurationBuilder<BuiltConfiguration> builder, String level, AppenderComponentBuilder console) {
    builder.add(console)
      .add(builder.newLogger("org.hibernate", Level.OFF)
        .add(builder.newAppenderRef(EXT_APPENDER))
        .addAttribute("additivity", false))
      .add(builder.newLogger("org.hibernate.SQL", Level.getLevel(level))
        .add(builder.newAppenderRef(EXT_APPENDER))
        .addAttribute("additivity", false))
      .add(builder.newLogger("org.hibernate.type.descriptor.sql", Level.getLevel(level))
        .add(builder.newAppenderRef(EXT_APPENDER))
        .addAttribute("additivity", false));
  }

  @Override
  public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
    return getConfiguration(loggerContext, source.toString(), null);
  }

  @Override
  public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
    ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
    return createConfiguration(name, builder);
  }

  @Override
  protected String[] getSupportedTypes() {
    return new String[]{"*"};
  }
}
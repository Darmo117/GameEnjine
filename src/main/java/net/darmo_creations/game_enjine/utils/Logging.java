package net.darmo_creations.game_enjine.utils;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Logging {
  /**
   * Create a logger with the given name.
   *
   * @param name Logger’s name.
   * @return The logger.
   */
  public static Logger getLogger(String name) {
    Logger logger = Logger.getLogger(name);
    logger.setUseParentHandlers(false);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new Formatter() {
      private static final String FORMAT = "[%1$tF %1$tT] [%2$s] [%3$s] %4$s%n";

      @Override
      public String format(LogRecord record) {
        return FORMAT.formatted(new Date(record.getMillis()), record.getLoggerName(), record.getLevel(), record.getMessage());
      }
    });
    logger.addHandler(handler);
    return logger;
  }

  private Logging() {
  }
}

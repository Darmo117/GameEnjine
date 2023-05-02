package net.darmo_creations.game_enjine.utils;

public class TimeUtils {
  /**
   * Returns the current time in seconds.
   */
  public static double getTime() {
    return System.nanoTime() / 1e9;
  }
}

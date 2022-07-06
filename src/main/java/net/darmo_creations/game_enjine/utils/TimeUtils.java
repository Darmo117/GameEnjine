package net.darmo_creations.game_enjine.utils;

public class TimeUtils {
  public static double getTime() {
    return System.nanoTime() / 1e9;
  }
}

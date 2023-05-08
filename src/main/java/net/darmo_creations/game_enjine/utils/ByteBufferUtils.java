package net.darmo_creations.game_enjine.utils;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class ByteBufferUtils {
  public static String getString(ByteBuf bb) {
    int length = bb.readInt();
    byte[] bytes = new byte[length];
    bb.readBytes(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public static void putString(ByteBuf bb, String s) {
    bb.writeInt(s.length());
    bb.writeBytes(s.getBytes(StandardCharsets.UTF_8));
  }

  @SuppressWarnings("unchecked")
  public static <T extends Enum<T>> T getEnumValue(ByteBuf bb, Class<T> enumClass) {
    int ordinal = bb.readInt();
    try {
      T[] values = (T[]) enumClass.getMethod("values").invoke(null);
      return values[ordinal % values.length];
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static void putEnumValue(ByteBuf bb, Enum<?> value) {
    bb.writeInt(value.ordinal());
  }

  private ByteBufferUtils() {
  }
}

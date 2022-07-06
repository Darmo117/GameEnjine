package net.darmo_creations.game_enjine;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public record EngineConfig(
    String name,
    String subtitle,
    String version,
    boolean grabCursor,
    boolean resizable
) {
  public static EngineConfig fromFile(final String path) throws IOException {
    //noinspection MismatchedQueryAndUpdateOfCollection
    Wini ini = new Wini(new File(path));
    String name = ini.get("Game", "Name", String.class);
    if (name == null || name.equals("")) {
      throw new IOException("game name not specified");
    }
    String subtitle = ini.get("Game", "Subtitle", String.class);
    if ("".equals(subtitle)) {
      subtitle = null;
    }
    String version = ini.get("Game", "Version", String.class);
    if (version == null || version.equals("")) {
      throw new IOException("game version not specified");
    }
    boolean grabCursor = ini.get("Window", "GrabCursor", boolean.class);
    boolean resizable = ini.get("Window", "Resizable", boolean.class);

    return new EngineConfig(
        name,
        subtitle,
        version,
        grabCursor,
        resizable
    );
  }
}

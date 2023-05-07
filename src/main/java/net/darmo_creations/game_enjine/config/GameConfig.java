package net.darmo_creations.game_enjine.config;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GameConfig {
  private final String name;
  private final String subtitle;
  private final String version;
  private final boolean grabCursor;
  private final boolean resizable;

  public GameConfig(File path) throws IOException {
    //noinspection MismatchedQueryAndUpdateOfCollection
    Wini ini = new Wini(path);
    String name = ini.get("Game", "Name", String.class);
    if (name == null || name.equals("")) {
      throw new IOException("game title not specified");
    }
    String subtitle = ini.get("Game", "Subtitle", String.class);
    if ("".equals(subtitle)) {
      subtitle = null;
    }
    String version = ini.get("Game", "Version", String.class);
    if (version == null || version.equals("")) {
      throw new IOException("game version not specified");
    }

    this.name = name;
    this.subtitle = subtitle;
    this.version = version;
    this.grabCursor = ini.get("Window", "GrabCursor", boolean.class);
    this.resizable = ini.get("Window", "Resizable", boolean.class);
  }

  public String name() {
    return this.name;
  }

  public String subtitle() {
    return this.subtitle;
  }

  public String version() {
    return this.version;
  }

  public boolean grabCursor() {
    return this.grabCursor;
  }

  public boolean resizable() {
    return this.resizable;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (GameConfig) obj;
    return Objects.equals(this.name, that.name) &&
        Objects.equals(this.subtitle, that.subtitle) &&
        Objects.equals(this.version, that.version) &&
        this.grabCursor == that.grabCursor &&
        this.resizable == that.resizable;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.subtitle, this.version, this.grabCursor, this.resizable);
  }

  @Override
  public String toString() {
    return "GameConfig[name=%s, subtitle=%s, version=%s, grabCursor=%s, resizable=%s]"
        .formatted(this.name, this.subtitle, this.version, this.grabCursor, this.resizable);
  }
}

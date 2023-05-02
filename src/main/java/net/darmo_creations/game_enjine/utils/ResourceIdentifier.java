package net.darmo_creations.game_enjine.utils;

import net.darmo_creations.game_enjine.GameEnjine;

import java.io.File;
import java.nio.file.Paths;

public record ResourceIdentifier(String path, String resourceName) {
  public ResourceIdentifier(String path) {
    this(path.substring(0, path.lastIndexOf('/')), path.substring(path.lastIndexOf('/') + 1));
  }

  public String fullPath() {
    return Paths.get(GameEnjine.DATA_DIR_PATH, this.path.replace("/", File.separator), this.resourceName).toString();
  }
}

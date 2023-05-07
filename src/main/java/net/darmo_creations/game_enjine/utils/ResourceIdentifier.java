package net.darmo_creations.game_enjine.utils;

import net.darmo_creations.game_enjine.GameEnjine;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public final class ResourceIdentifier {
  private final String path;
  private final String resourceName;
  private final String fullPath;

  public ResourceIdentifier(String path) {
    this(path.substring(0, path.lastIndexOf('/')), path.substring(path.lastIndexOf('/') + 1));
  }

  public ResourceIdentifier(String path, String resourceName) {
    this.path = path;
    this.resourceName = resourceName;
    this.fullPath = Paths.get(
        GameEnjine.DATA_DIR_PATH.getPath(),
        this.path.replace("/", File.separator),
        resourceName
    ).toString();
  }

  public String path() {
    return this.path;
  }

  public String resourceName() {
    return this.resourceName;
  }

  public String fileSystemPath() {
    return this.fullPath;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (ResourceIdentifier) obj;
    return Objects.equals(this.path, that.path) &&
        Objects.equals(this.resourceName, that.resourceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.path, this.resourceName);
  }

  @Override
  public String toString() {
    return "ResourceIdentifier[path=%s, resourceName=%s]".formatted(this.path, this.resourceName);
  }

}

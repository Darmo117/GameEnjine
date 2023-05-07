package net.darmo_creations.game_enjine.config;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class UserConfig {
  private int windowWidth;
  private int windowHeight;
  private boolean fullScreen;

  public UserConfig(File path) throws IOException {
    //noinspection MismatchedQueryAndUpdateOfCollection
    Wini ini = new Wini(path);
    this.windowWidth = ini.get("Window", "Width", int.class);
    if (this.windowWidth < 100) {
      throw new IllegalArgumentException("window width is too small");
    }
    this.windowHeight = ini.get("Window", "Height", int.class);
    if (this.windowHeight < 100) {
      throw new IllegalArgumentException("window height is too small");
    }
    this.fullScreen = ini.get("Window", "FullScreen", boolean.class);
  }

  public int windowWidth() {
    return this.windowWidth;
  }

  public void setWindowWidth(int windowWidth) {
    this.windowWidth = windowWidth;
  }

  public int windowHeight() {
    return this.windowHeight;
  }

  public void setWindowHeight(int windowHeight) {
    this.windowHeight = windowHeight;
  }

  public boolean isFullScreen() {
    return this.fullScreen;
  }

  public void setFullScreen(boolean fullScreen) {
    this.fullScreen = fullScreen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    UserConfig that = (UserConfig) o;
    return this.windowWidth == that.windowWidth && this.windowHeight == that.windowHeight && this.fullScreen == that.fullScreen;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.windowWidth, this.windowHeight, this.fullScreen);
  }
}

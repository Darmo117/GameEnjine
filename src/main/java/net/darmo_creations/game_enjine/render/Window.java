package net.darmo_creations.game_enjine.render;

import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.input.InputHandler;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
  private long windowPointer;
  private int width, height;
  private boolean fullscreen;
  private boolean hasResized;
  private GLFWWindowSizeCallback windowSizeCallback;
  private InputHandler inputHandler;

  public Window(int width, int height, boolean fullscreen, boolean resizable) {
    // Configure GLFW
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Do not show window until glfwShowWindow is called
    glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
    this.setSize(width, height);
    this.setFullscreen(fullscreen);
    this.hasResized = false;
  }

  public void show(final String title) {
    this.windowPointer = glfwCreateWindow(this.width, this.height, title, this.fullscreen ? glfwGetPrimaryMonitor() : 0, 0);

    if (this.windowPointer == 0) {
      throw new IllegalStateException("Failed to create window");
    }

    if (!this.fullscreen) {
      GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
      //noinspection ConstantConditions
      glfwSetWindowPos(this.windowPointer, (vid.width() - this.width) / 2, (vid.height() - this.height) / 2);
    }

    // Icon
    GLFWImage image = GLFWImage.malloc();
    GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
    ByteImage windowIcon;
    try {
      windowIcon = ByteImage.load(new File(GameEnjine.DATA_DIR_PATH, "icon.png").toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    image.set(windowIcon.width(), windowIcon.height(), windowIcon.image());
    imagebf.put(0, image);
    glfwSetWindowIcon(this.windowPointer, imagebf);

    glfwShowWindow(this.windowPointer);
    glfwMakeContextCurrent(this.windowPointer);
    glfwSwapInterval(1); // VSync

    this.inputHandler = new InputHandler(this.windowPointer);
    this.setLocalCallbacks();
  }

  private void setLocalCallbacks() {
    this.windowSizeCallback = new GLFWWindowSizeCallback() {
      @Override
      public void invoke(long windowPointer, int width, int height) {
        Window.this.width = width;
        Window.this.height = height;
        Window.this.hasResized = true;
      }
    };
    glfwSetWindowSizeCallback(this.windowPointer, this.windowSizeCallback);
  }

  public void cleanUp() {
    glfwFreeCallbacks(this.windowPointer);
    this.windowSizeCallback.close();
    glfwDestroyWindow(this.windowPointer);
  }

  public boolean shouldClose() {
    return glfwWindowShouldClose(this.windowPointer);
  }

  public void swapBuffers() {
    glfwSwapBuffers(this.windowPointer);
  }

  public void grabCursor() {
    glfwSetInputMode(this.windowPointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
  }

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
  }

  public void update() {
    this.hasResized = false;
    this.inputHandler.update();
    glfwPollEvents();
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public boolean hasResized() {
    return this.hasResized;
  }

  public boolean isFullscreen() {
    return this.fullscreen;
  }

  public long getWindowPointer() {
    return this.windowPointer;
  }

  public InputHandler getInputHandler() {
    return this.inputHandler;
  }
}

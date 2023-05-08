package net.darmo_creations.game_enjine.input;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
  private static final int FIRST_KEY = GLFW_KEY_SPACE;

  private final long windowPointer;
  private final boolean[] downKeys = new boolean[GLFW_KEY_LAST];
  private final boolean[] downMouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];

  public InputHandler(long windowPointer) {
    this.windowPointer = windowPointer;
    for (int i = FIRST_KEY; i < GLFW_KEY_LAST; i++) {
      this.downKeys[i] = false;
    }
    Arrays.fill(this.downMouseButtons, false);
  }

  public boolean isKeyDown(int key) {
    return glfwGetKey(this.windowPointer, key) == GLFW_PRESS;
  }

  public boolean isMouseButtonDown(int button) {
    return glfwGetMouseButton(this.windowPointer, button) == GLFW_PRESS;
  }

  public boolean isKeyPressed(int key) {
    return this.isKeyDown(key) && !this.downKeys[key];
  }

  public boolean isMouseButtonPressed(int button) {
    return this.isMouseButtonDown(button) && !this.downMouseButtons[button];
  }

  public void update() {
    for (int i = FIRST_KEY; i < GLFW_KEY_LAST; i++) {
      this.downKeys[i] = this.isKeyDown(i);
    }
    for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
      this.downMouseButtons[i] = this.isMouseButtonDown(i);
    }
    glfwPollEvents();
  }
}

package net.darmo_creations.game_enjine;

import net.darmo_creations.game_enjine.config.GameConfig;
import net.darmo_creations.game_enjine.config.UserConfig;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;
import net.darmo_creations.game_enjine.scene.Scene;
import net.darmo_creations.game_enjine.scene.SceneAction;
import net.darmo_creations.game_enjine.utils.TimeUtils;
import net.darmo_creations.game_enjine.world.Tile;
import net.darmo_creations.game_enjine.world.WorldLoader;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameEnjine {
  public static final String NAME = "GameEnjine";
  public static final String VERSION = "1.0";

  public static final File GAME_INI_FILE_PATH = new File("game.ini");
  public static final File USER_INI_FILE_PATH = new File("user.ini");
  public static final File DATA_DIR_PATH = new File("data");

  private final GameConfig config;
  private final UserConfig userConfig;

  private final Window window;
  private final Shader globalShader;

  private final WorldLoader worldLoader;
  private Scene scene;

  public GameEnjine() throws IOException {
    glfwSetErrorCallback((error, description) -> {
      throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
    });
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    this.config = new GameConfig(GAME_INI_FILE_PATH);
    this.userConfig = new UserConfig(USER_INI_FILE_PATH);
    this.window = new Window(this.userConfig.windowWidth(), this.userConfig.windowHeight(), this.userConfig.isFullScreen(), this.config.resizable());
    this.initWindow();
    try {
      this.globalShader = new Shader("shader");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Tile.loadTiles();
    this.worldLoader = new WorldLoader();
  }

  private void initWindow() {
    String title = this.config.name();
    if (this.config.subtitle() != null) {
      title += " - " + this.config.subtitle();
    }
    title += " (v%s)".formatted(this.config.version());
    this.window.show(title);
    if (this.config.grabCursor()) {
      this.window.grabCursor();
    }
    GL.createCapabilities(); // Needed to make LWJGL and GLFW interoperable
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glClearColor(0, 0, 0, 0);
    glEnable(GL_TEXTURE_2D);
  }

  public Window window() {
    return this.window;
  }

  public GameConfig config() {
    return this.config;
  }

  public UserConfig userConfig() {
    return this.userConfig;
  }

  public void transitionToScene(Scene scene) {
    this.scene = scene;
  }

  public void loadWorld(String name) {
    try {
      this.scene = this.worldLoader.loadWorld(name);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.scene.onWindowResized(this.window);
  }

  public void run() {
    System.out.printf("Running %s v%s with LWJGL %s%n", NAME, VERSION, Version.getVersion());
    this.loop();
    this.cleanUp();
  }

  private void loop() {
    // TEMP
    this.loadWorld("map1");

    // Cap FPS
    final double FRAME_CAP = 1 / 60.0; // 60 FPS
    double prevTime = TimeUtils.getTime();
    double unprocessed = 0;
    double frameTime = 0;
    int fps = 0;
    boolean canRender = false;

    while (!this.window.shouldClose()) {
      double now = TimeUtils.getTime();
      double elapsed = now - prevTime;
      unprocessed += elapsed;
      frameTime += elapsed;
      prevTime = now;

      while (unprocessed >= FRAME_CAP) {
        unprocessed -= FRAME_CAP;
        canRender = true;
        if (this.window.hasResized()) {
          this.scene.onWindowResized(this.window);
          glViewport(0, 0, this.window.width(), this.window.height());
        }
        Optional<SceneAction> action = this.scene.update(this.window);
        action.ifPresent(a -> a.execute(this));
        this.window.update();
        if (frameTime >= 1) {
          System.out.println("FPS: " + fps);
          frameTime = 0;
          fps = 0;
        }
      }

      if (canRender) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        this.scene.render(this.globalShader, this.window);
        this.window.swapBuffers();
        fps++;
      }
    }
  }

  private void cleanUp() {
    this.globalShader.delete();
    this.window.cleanUp();
    glfwTerminate();
    //noinspection ConstantConditions
    glfwSetErrorCallback(null).free();
  }
}

package net.darmo_creations.game_engine;

import net.darmo_creations.game_engine.render.Camera;
import net.darmo_creations.game_engine.render.Shader;
import net.darmo_creations.game_engine.render.Window;
import net.darmo_creations.game_engine.world.TileRenderer;
import net.darmo_creations.game_engine.world.World;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameEnjine {
  public static final String NAME = "GameEnjine";
  public static final String VERSION = "1.0";

  public static final String GAME_INI_FILE_PATH = "game.ini";
  public static final String USER_INI_FILE_PATH = "user.ini";
  public static final String DATA_DIR_PATH = "data";

  private final EngineConfig config;
  private final UserConfig userConfig;

  private final Window window;
  private final Camera camera;
  private final TileRenderer tileRenderer;
  private final Shader globalShader;

  private GameState state;

  public GameEnjine() throws IOException {
    glfwSetErrorCallback((error, description) -> {
      throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
    });
    this.config = EngineConfig.fromFile(GAME_INI_FILE_PATH);
    this.userConfig = new UserConfig(USER_INI_FILE_PATH);
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    this.window = new Window(this.userConfig.windowWidth(), this.userConfig.windowHeight(), this.userConfig.fullScreen(), this.config.resizable());
    this.initWindow();
    GL.createCapabilities(); // Needed to make LWJGL and GLFW interoperable
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glClearColor(0, 0, 0, 0);
    glEnable(GL_TEXTURE_2D);
    this.camera = new Camera(this.userConfig.windowWidth(), this.userConfig.windowHeight());
    this.tileRenderer = new TileRenderer();
    try {
      this.globalShader = new Shader("shader");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.setState(GameState.MENU);
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
  }

  public EngineConfig getConfig() {
    return this.config;
  }

  public UserConfig getUserConfig() {
    return this.userConfig;
  }

  public GameState getState() {
    return this.state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

  public void run() {
    System.out.printf("Running %s v%s with LWJGL %s%n", NAME, VERSION, Version.getVersion());
    this.loop();
    this.cleanUp();
  }

  private void loop() {
    World world = new World(20, 20);
    world.calculateView(this.window);

    // Cap FPS
    final double FRAME_CAP = 1 / 60.0; // 60 FPS
    double prevTime = Utils.getTime();
    double unprocessed = 0;
    double frameTime = 0;
    int fps = 0;
    boolean canRender = false;

    while (!this.window.shouldClose()) {
      double now = Utils.getTime();
      double elapsed = now - prevTime;
      unprocessed += elapsed;
      frameTime += elapsed;
      prevTime = now;

      while (unprocessed >= FRAME_CAP) {
        unprocessed -= FRAME_CAP;
        canRender = true;
        if (this.window.hasResized()) {
          this.camera.setProjection(this.window.getWidth(), this.window.getHeight());
          world.calculateView(this.window);
          glViewport(0, 0, this.window.getWidth(), this.window.getHeight());
        }
        if (this.window.getInputHandler().isKeyDown(GLFW_KEY_ESCAPE)) {
          glfwSetWindowShouldClose(this.window.getWindowPointer(), true);
        }
        this.pollEvents();
        world.update(this.window, this.camera);
        this.window.update();
        if (frameTime >= 1) {
          System.out.println("FPS: " + fps);
          frameTime = 0;
          fps = 0;
        }
      }

      if (canRender) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        world.render(this.tileRenderer, this.globalShader, this.camera, this.window);
        this.window.swapBuffers();
        fps++;
      }
    }
  }

  private void pollEvents() {
    float speed = 10;
    if (this.window.getInputHandler().isKeyDown(GLFW_KEY_A)) {
      this.camera.getPosition().sub(new Vector3f(-speed, 0, 0));
    }
    if (this.window.getInputHandler().isKeyDown(GLFW_KEY_D)) {
      this.camera.getPosition().sub(new Vector3f(speed, 0, 0));
    }
    if (this.window.getInputHandler().isKeyDown(GLFW_KEY_W)) {
      this.camera.getPosition().sub(new Vector3f(0, speed, 0));
    }
    if (this.window.getInputHandler().isKeyDown(GLFW_KEY_S)) {
      this.camera.getPosition().sub(new Vector3f(0, -speed, 0));
    }
  }

  private void cleanUp() {
    this.window.cleanUp();
    glfwTerminate();
    //noinspection ConstantConditions
    glfwSetErrorCallback(null).free();
  }
}

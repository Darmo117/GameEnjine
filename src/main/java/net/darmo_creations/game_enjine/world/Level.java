package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.entities.PlayerEntity;
import net.darmo_creations.game_enjine.entities.render.EntityRenderer;
import net.darmo_creations.game_enjine.input.InputHandler;
import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;
import net.darmo_creations.game_enjine.scene.Scene;
import net.darmo_creations.game_enjine.scene.SceneAction;
import net.darmo_creations.game_enjine.utils.Color;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import net.darmo_creations.game_enjine.world.render.TileRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;

public class Level extends Scene {
  public static final int TILE_SIZE = 32;

  private final GameEnjine engine;
  private final int width;
  private final int height;
  private int tileScale;
  private final Vector2i defaultSpawnLocation;
  private final Tile[][][] tiles;
  private final TileInteraction[][] tileInteractions;
  private final int entityLayer;
  private final Color bgColor;
  private final List<Entity> entities;
  private PlayerEntity player;

  private final Camera camera;
  private Matrix4f scaleMatrix;

  private final TileRenderer tileRenderer;
  private final EntityRenderer entityRenderer;

  Level(
      GameEnjine engine,
      String name,
      int width,
      int height,
      Vector2i defaultSpawnLocation,
      final Tile[][][] tiles,
      final TileInteraction[][] tileInteractions,
      int entityLayer,
      int bgColor
  ) {
    super(name);
    this.engine = engine;
    this.width = width;
    this.height = height;
    this.defaultSpawnLocation = new Vector2i(defaultSpawnLocation);
    this.tiles = tiles;
    this.tileInteractions = tileInteractions;
    this.entityLayer = entityLayer;
    this.entities = new ArrayList<>();
    this.setScale(2);
    this.bgColor = new Color(bgColor);
    this.tileRenderer = new TileRenderer();
    this.entityRenderer = new EntityRenderer();
    this.camera = new Camera();
  }

  public void spawnEntities(final Vector2i playerLocation) {
    // TEMP
    this.entities.add(this.player = new PlayerEntity(
        this,
        new Vector2f(1, 1),
        new Vector2f(playerLocation),
        "Character.png"
    ));
    this.player.setSpeed(0.05f);
  }

  public Vector2i defaultSpawnLocation() {
    return new Vector2i(this.defaultSpawnLocation);
  }

  public void setScale(int scale) {
    if (scale <= 0) {
      throw new IllegalArgumentException("scale must be > 0");
    }
    this.tileScale = scale * TILE_SIZE;
    this.scaleMatrix = new Matrix4f().scale(this.tileScale);
  }

  public Tile getTile(int x, int y, int layer) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      throw new ArrayIndexOutOfBoundsException(x + " " + y);
    }
    return this.tiles[layer][this.height - y - 1][x];
  }

  public Optional<TileInteraction> getTileInteraction(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      return Optional.empty();
    }
    return Optional.of(this.tileInteractions[this.height - y - 1][x]);
  }

  @Override
  public Optional<SceneAction> update(final Window window) {
    this.pollEvents(window);
    this.camera.setPosition(this.player.position().mul(this.tileScale, new Vector2f()));
    this.capCameraPosition(window);
    this.entities.forEach(Entity::update);
    return Optional.empty();
  }

  @Override
  public void onWindowResized(final Window window) {
    this.camera.setProjection(window.width(), window.height());
  }

  @Override
  public void render(Shader shader, final Window window) {
    GL11.glClearColor(this.bgColor.r(), this.bgColor.g(), this.bgColor.b(), 0);
    for (int layer = 0; layer < this.tiles.length; layer++) {
      for (int x = 0; x < this.width; x++) {
        for (int y = 0; y < this.height; y++) {
          // TODO culling
          Tile tile = this.getTile(x, y, layer);
          if (tile != null) {
            this.tileRenderer.renderTile(
                this.engine.textureManager(), tile, shader, x, y, this.scaleMatrix, this.camera);
          }
        }
      }
      if (layer == this.entityLayer) {
        // TODO culling
        this.entities.forEach(entity -> this.entityRenderer.renderEntity(
            this.engine.textureManager(), entity, shader, this.scaleMatrix, this.camera));
      }
    }
  }

  private void pollEvents(final Window window) {
    InputHandler inputHandler = window.inputHandler();
    if (inputHandler.isKeyDown(GLFW_KEY_ESCAPE)) {
      window.setShouldClose();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_W)) {
      this.player.goUp();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_S)) {
      this.player.goDown();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_A)) {
      this.player.goLeft();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_D)) {
      this.player.goRight();
    }
  }

  private void capCameraPosition(final Window window) {
    Vector2f pos = this.camera.position();
    int w = this.width * this.tileScale;
    int h = this.height * this.tileScale;

    if (w <= window.width()) {
      pos.x = w / 2f;
    }
    if (h <= window.height()) {
      pos.y = h / 2f;
    }
  }
}

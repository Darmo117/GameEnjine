package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.entities.PlayerEntity;
import net.darmo_creations.game_enjine.entities.render.EntityRenderer;
import net.darmo_creations.game_enjine.input.InputHandler;
import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;
import net.darmo_creations.game_enjine.scene.Scene;
import net.darmo_creations.game_enjine.scene.SceneAction;
import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class World extends Scene {
  public static final int TILE_SIZE = 16;

  private final int width;
  private final int height;
  private int tileScale;
  private final Tile[] tiles;
  private final TileInteraction[] tileInteractions;
  private final List<Entity> entities;
  private final PlayerEntity player;

  private final Camera camera;
  private Matrix4f scaleMatrix;

  private final TileRenderer tileRenderer;
  private final EntityRenderer entityRenderer;

  World(String name, int width, int height, final Tile[] tiles, final TileInteraction[] tileInteractions) {
    super(name);
    this.width = width;
    this.height = height;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.tileInteractions = Arrays.copyOf(tileInteractions, tileInteractions.length);
    this.entities = new ArrayList<>();
    this.setScale(2);
    this.tileRenderer = new TileRenderer();
    this.entityRenderer = new EntityRenderer();
    this.camera = new Camera();

    // TEMP
    this.entities.add(this.player = new PlayerEntity(
        this,
        new Vector2f(1, 1),
        new Vector2f(3, 0),
        Collections.singletonList(new ResourceIdentifier("textures/entities/32x32/Character_001"))
    ));
    this.player.setSpeed(0.05f);
  }

  public void setScale(int scale) {
    if (scale <= 0) {
      throw new IllegalArgumentException("scale must be > 0");
    }
    this.tileScale = scale * TILE_SIZE;
    this.scaleMatrix = new Matrix4f().scale(this.tileScale);
  }

  public Tile getTile(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      throw new ArrayIndexOutOfBoundsException(x + " " + y);
    }
    return this.tiles[x + (this.height - y - 1) * this.width];
  }

  public Optional<TileInteraction> getTileInteraction(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      return Optional.empty();
    }
    return Optional.of(this.tileInteractions[x + (this.height - y - 1) * this.width]);
  }

  @Override
  public Optional<SceneAction> update(final Window window) {
    this.camera.setPosition(this.player.position());
    this.pollEvents(window);
    this.entities.forEach(Entity::update);
    return Optional.empty();
  }

  @Override
  public void onWindowResized(final Window window) {
    this.camera.setProjection(window.width(), window.height());
  }

  @Override
  public void render(Shader shader, final Window window) {
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        this.tileRenderer.renderTile(this.getTile(x, y), shader, new Vector2f(x, y), this.scaleMatrix, this.camera);
      }
    }
    this.entities.forEach(entity -> this.entityRenderer.renderEntity(entity, shader, this.scaleMatrix, this.camera));
  }

  private void pollEvents(final Window window) {
    InputHandler inputHandler = window.inputHandler();
    if (inputHandler.isKeyDown(GLFW_KEY_A)) {
      this.player.goLeft();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_D)) {
      this.player.goRight();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_W)) {
      this.player.goUp();
    }
    if (inputHandler.isKeyDown(GLFW_KEY_S)) {
      this.player.goDown();
    }
    this.capCameraPosition(window);
  }

  private void capCameraPosition(final Window window) {
    Vector2f pos = this.camera.position();
    int w = this.width * this.tileScale;
    int h = this.height * this.tileScale;

    if (w <= window.width()) {
      pos.x = (w - this.tileScale) / 2f;
    }
    if (h <= window.height()) {
      pos.y = (h - this.tileScale) / 2f;
    }
  }
}

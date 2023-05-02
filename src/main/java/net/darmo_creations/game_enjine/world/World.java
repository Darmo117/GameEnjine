package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.config.GameState;
import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.entities.PlayerEntity;
import net.darmo_creations.game_enjine.entities.render.EntityRenderer;
import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;
import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class World {
  private static final int TILE_SCALE = 8;

  private final String name;
  private final int width;
  private final int height;
  private final int[] tiles;
  private final TileInteraction[] tileInteractions;
  private final List<Entity> entities;
  private int scale;
  private int viewX, viewY;

  private final Camera camera;
  private final Matrix4f scaleMatrix;

  private final TileRenderer tileRenderer;
  private final EntityRenderer entityRenderer;

  public World(String name, int width, int height, final int[] tiles, final TileInteraction[] tileInteractions, GameState gameState) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.tileInteractions = Arrays.copyOf(tileInteractions, tileInteractions.length);
    this.entities = new ArrayList<>();
    this.setScale(3);
    this.scaleMatrix = new Matrix4f().scale(this.scale);
    this.tileRenderer = new TileRenderer();
    this.entityRenderer = new EntityRenderer();
    this.camera = new Camera();

    // TEMP
    this.entities.add(new PlayerEntity(new Vector2f(1, 2), new Vector2f(1, 1), Collections.singletonList(
        new ResourceIdentifier("textures/entities/32x32/Character_001"))));
  }

  public String name() {
    return this.name;
  }

  public int scale() {
    return this.scale;
  }

  public void setScale(int scale) {
    if (scale <= 0) {
      throw new IllegalArgumentException("scale must be > 0");
    }
    this.scale = scale * TILE_SCALE;
  }

  public Camera camera() {
    return this.camera;
  }

  public TileInteraction getTileInteraction(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      return null;
    }
    return this.tileInteractions[x + y * this.width];
  }

  public Tile getTile(int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      return null;
    }
    return Tile.TILES.get(this.tiles[x + y * this.width]);
  }

  public void setTile(Tile tile, int x, int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      throw new ArrayIndexOutOfBoundsException(x + " " + y);
    }
    this.tiles[x + y * this.width] = tile.id();
  }

  public void calculateView(final Window window) {
    this.viewX = (window.width() / (this.scale * 2)) + 4;
    this.viewY = (window.height() / (this.scale * 2)) + 4;
    this.camera.setProjection(window.width(), window.height());
  }

  public void render(Shader shader, final Window window) {
    int posX = ((int) this.camera.position().x + window.width() / 2) / (this.scale * 2);
    int posY = ((int) this.camera.position().y - window.height() / 2) / (this.scale * 2);
    for (int x = 0; x < this.viewX; x++) {
      for (int y = 0; y < this.viewY; y++) {
        Tile t = this.getTile(x - posX, y + posY);
        if (t != null) {
          this.tileRenderer.renderTile(t, x - posX, -y - posY, shader, this.scaleMatrix, this.camera);
        }
      }
    }
    System.out.println(posX + " " + posY); // DEBUG

    this.entities.forEach(entity -> this.entityRenderer.renderEntity(entity, posX, posY, shader, this.scaleMatrix, this.camera));
  }

  public void update(final Window window) {
    this.capCameraPosition(window);
  }

  private void capCameraPosition(final Window window) {
    Vector3f pos = this.camera.position();
    int w = this.width * this.scale * 2;
    int h = this.height * this.scale * 2;

    if (w < window.width()) {
      pos.x = -w / 2f + this.scale;
    } else {
      float halfWidth = window.width() / 2f;
      if (pos.x > -halfWidth + this.scale) {
        pos.x = -halfWidth + this.scale;
      }
      if (pos.x < -w + halfWidth + this.scale) {
        pos.x = -w + halfWidth + this.scale;
      }
    }

    if (h < window.height()) {
      pos.y = h / 2f - this.scale;
    } else {
      float halfHeight = window.height() / 2f;
      if (pos.y < halfHeight - this.scale) {
        pos.y = halfHeight - this.scale;
      }
      if (pos.y > h - halfHeight - this.scale) {
        pos.y = h - halfHeight - this.scale;
      }
    }
  }
}

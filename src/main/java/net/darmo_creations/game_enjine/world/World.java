package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

public class World {
  private static final int TILE_SCALE = 8;

  private final int[] tiles;
  private final int width, height;
  private int scale;
  private int viewX, viewY;

  private final Matrix4f matrix;

  private final TileRenderer tileRenderer;
  private final Camera camera;

  public World(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.setScale(3);
    this.matrix = new Matrix4f().scale(this.scale);
    this.tileRenderer = new TileRenderer();
    this.camera = new Camera();
    this.tiles = new int[width * height];
    // TEMP
    Arrays.fill(this.tiles, Tile.TEST_TILE.getID());
    this.setTile(Tile.TEST_TILE_2, 0, 0);
    this.setTile(Tile.TEST_TILE_2, width - 1, height - 1);
  }

  public int getScale() {
    return this.scale;
  }

  public void setScale(int scale) {
    if (scale <= 0) {
      throw new IllegalArgumentException("scale must be > 0");
    }
    this.scale = scale * TILE_SCALE;
  }

  public Camera getCamera() {
    return this.camera;
  }

  public Tile getTile(final int x, final int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      return null;
    }
    return Tile.TILES[this.tiles[x + y * this.width]];
  }

  public void setTile(Tile tile, final int x, final int y) {
    if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
      throw new ArrayIndexOutOfBoundsException(x + " " + y);
    }
    this.tiles[x + y * this.width] = tile.getID();
  }

  public void calculateView(final Window window) {
    this.viewX = (window.getWidth() / (this.scale * 2)) + 4;
    this.viewY = (window.getHeight() / (this.scale * 2)) + 4;
    this.camera.setProjection(window.getWidth(), window.getHeight());
  }

  public void render(final Shader shader, final Window window) {
    int posX = ((int) this.camera.getPosition().x + window.getWidth() / 2) / (this.scale * 2);
    int posY = ((int) this.camera.getPosition().y - window.getHeight() / 2) / (this.scale * 2);
    for (int x = 0; x < this.viewX; x++) {
      for (int y = 0; y < this.viewY; y++) {
        Tile t = this.getTile(x - posX, y + posY);
        if (t != null) {
          this.tileRenderer.renderTile(t, x - posX, -y - posY, shader, this.matrix, this.camera);
        }
      }
    }
  }

  public void update(final Window window) {
    this.capCameraPosition(window);
  }

  private void capCameraPosition(final Window window) {
    Vector3f pos = this.camera.getPosition();
    int w = this.width * this.scale * 2;
    int h = this.height * this.scale * 2;

    if (w < window.getWidth()) {
      pos.x = -w / 2f + this.scale;
    } else {
      float halfWidth = window.getWidth() / 2f;
      if (pos.x > -halfWidth + this.scale) {
        pos.x = -halfWidth + this.scale;
      }
      if (pos.x < -w + halfWidth + this.scale) {
        pos.x = -w + halfWidth + this.scale;
      }
    }

    if (h < window.getHeight()) {
      pos.y = h / 2f - this.scale;
    } else {
      float halfHeight = window.getHeight() / 2f;
      if (pos.y < halfHeight - this.scale) {
        pos.y = halfHeight - this.scale;
      }
      if (pos.y > h - halfHeight - this.scale) {
        pos.y = h - halfHeight - this.scale;
      }
    }
  }
}

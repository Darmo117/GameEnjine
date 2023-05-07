package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.render.*;
import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class TileRenderer {
  private final Map<ResourceIdentifier, Texture> tileTextures;
  private final Model tileModel;

  public TileRenderer() {
    this.tileTextures = new HashMap<>();
    this.tileModel = new QuadModel(new Vector2f(1), new float[]{
        0, 0,
        1, 0,
        1, 1,
        0, 1,
    });

    for (Tile tile : Tile.TILES.values()) {
      ResourceIdentifier resource = tile.texture();
      if (!this.tileTextures.containsKey(resource)) {
        this.tileTextures.put(resource, new Texture(resource));
      }
    }
  }

  public void renderTile(Tile tile, Shader shader, final Vector2f pos, final Matrix4f scaleMatrix, final Camera camera) {
    shader.bind();
    if (this.tileTextures.containsKey(tile.texture())) {
      this.tileTextures.get(tile.texture()).bind(0);
    }

    Matrix4f tilePos = new Matrix4f().translate(new Vector3f(pos.x(), pos.y(), 0));
    Matrix4f projection = camera.projection().mul(scaleMatrix, new Matrix4f());
    projection.mul(tilePos);

    shader.setSampler(0);
    shader.setProjection(projection);

    this.tileModel.render();
  }
}

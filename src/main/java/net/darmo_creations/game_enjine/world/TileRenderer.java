package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Model;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class TileRenderer {
  private final Map<String, Texture> tileTextures;
  private final Model tileModel;

  public TileRenderer() {
    this.tileTextures = new HashMap<>();
    float[] vertices = {
        -1f, 1f, 0, // TOP LEFT 0
        1f, 1f, 0,  // TOP RIGHT 1
        1f, -1f, 0, // BOTTOM RIGHT 2
        -1f, -1f, 0,// BOTTOM LEFT 3
    };
    float[] texture = {
        0, 0,
        1, 0,
        1, 1,
        0, 1,
    };
    int[] indices = {
        0, 1, 2,
        2, 3, 0,
    };

    this.tileModel = new Model(vertices, texture, indices);

    for (Tile tile : Tile.TILES.values()) {
      String tex = tile.getTexture();
      if (!this.tileTextures.containsKey(tex)) {
        this.tileTextures.put(tex, new Texture(tex + ".png"));
      }
    }
  }

  public void renderTile(final Tile tile, final int x, final int y,
                         final Shader shader, final Matrix4f matrix, final Camera camera) {
    shader.bind();
    if (this.tileTextures.containsKey(tile.getTexture())) {
      this.tileTextures.get(tile.getTexture()).bind(0);
    }

    Matrix4f tilePos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
    Matrix4f projection = new Matrix4f();
    camera.getProjection().mul(matrix, projection);
    projection.mul(tilePos);

    shader.setUniform("sampler", 0);
    shader.setUniform("projection", projection);

    this.tileModel.render();
  }
}

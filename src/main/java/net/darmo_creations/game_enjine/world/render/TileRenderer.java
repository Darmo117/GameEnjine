package net.darmo_creations.game_enjine.world.render;

import net.darmo_creations.game_enjine.render.*;
import net.darmo_creations.game_enjine.world.Tile;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TileRenderer {
  private final Model tileModel;

  public TileRenderer() {
    this.tileModel = new QuadModel(new Vector2f(1), new float[] {
        0, 0,
        1, 0,
        1, 1,
        0, 1,
    });
  }

  public void renderTile(TextureManager textureManager, Tile tile, Shader shader, int x, int y,
                         final Matrix4f scaleMatrix, final Camera camera) {
    shader.bind();
    textureManager.getTexture(tile.tilesetID(), tile.tileID()).bind(0);
    Matrix4f tilePos = new Matrix4f().translate(new Vector3f(x, y, 0));
    Matrix4f projection = camera.projection().mul(scaleMatrix, new Matrix4f());
    projection.mul(tilePos);
    shader.setSampler(0);
    shader.setProjection(projection);
    this.tileModel.render();
  }
}

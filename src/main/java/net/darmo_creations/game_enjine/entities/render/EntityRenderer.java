package net.darmo_creations.game_enjine.entities.render;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.render.Camera;
import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Texture;
import net.darmo_creations.game_enjine.render.TextureManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityRenderer {
  public void renderEntity(TextureManager textureManager, final Entity entity, Shader shader,
                           final Matrix4f scaleMatrix, final Camera camera) {
    shader.bind();
    Texture[] textures = textureManager.getEntityTextures(entity.spritesheet());
    Texture texture;
    if (textures.length == 1) {
      texture = textures[0];
    } else {
      texture = textures[entity.direction()];
    }
    texture.bind(0);

    Matrix4f tilePos = new Matrix4f().translate(new Vector3f(entity.position(), 0));
    Matrix4f projection = camera.projection().mul(scaleMatrix, new Matrix4f());
    projection.mul(tilePos);

    shader.setSampler(0);
    shader.setProjection(projection);

    entity.model().render();
  }
}

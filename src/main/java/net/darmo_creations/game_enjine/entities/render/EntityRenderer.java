package net.darmo_creations.game_enjine.entities.render;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.render.*;
import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class EntityRenderer {
  private final Map<String, Map<ResourceIdentifier, Texture>> textures = new HashMap<>();
  private final Map<String, Map<Vector2f, Model>> models = new HashMap<>();

  public void renderEntity(final Entity entity, Shader shader, final Matrix4f scaleMatrix, final Camera camera) {
    String entityType = entity.type();
    Vector2f position = entity.position();
    Vector2f size = entity.size();
    ResourceIdentifier texture = entity.texture();

    shader.bind();
    if (!this.textures.containsKey(entityType)) {
      this.textures.put(entityType, new HashMap<>());
    }
    Map<ResourceIdentifier, Texture> entityTextures = this.textures.get(entityType);
    if (!entityTextures.containsKey(texture)) {
      entityTextures.put(texture, new Texture(texture));
    }
    this.textures.get(entityType).get(texture).bind(0);

    Matrix4f tilePos = new Matrix4f().translate(new Vector3f(position.x, position.y, 0));
    Matrix4f projection = camera.projection().mul(scaleMatrix, new Matrix4f());
    projection.mul(tilePos);

    shader.setSampler(0);
    shader.setProjection(projection);

    if (!this.models.containsKey(entityType)) {
      this.models.put(entityType, new HashMap<>());
    }
    Map<Vector2f, Model> entityModels = this.models.get(entityType);
    if (!entityModels.containsKey(size)) {
      entityModels.put(size, new QuadModel(size, new float[]{
          0, 0,
          1, 0,
          1, 1,
          0, 1,
      }));
    }
    this.models.get(entityType).get(size).render();
  }
}

package net.darmo_creations.game_enjine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
  private final Vector2f position;
  private Matrix4f projection;

  public Camera() {
    this.position = new Vector2f();
  }

  public Vector2f position() {
    return this.position;
  }

  public void setPosition(final Vector2f position) {
    this.position.set(position);
  }

  public Matrix4f projection() {
    return this.projection.translate(new Vector3f(this.position, 0).negate(new Vector3f()), new Matrix4f());
  }

  public void setProjection(int width, int height) {
    this.projection = new Matrix4f().setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f);
  }
}

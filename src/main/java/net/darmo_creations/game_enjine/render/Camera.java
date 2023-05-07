package net.darmo_creations.game_enjine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
  private final Vector3f position;
  private Matrix4f projection;

  public Camera() {
    this.position = new Vector3f();
  }

  public Vector3f position() {
    return this.position;
  }

  public void setPosition(final Vector3f position) {
    this.position.set(position);
  }

  public Matrix4f projection() {
    return this.projection.translate(this.position.negate(new Vector3f()), new Matrix4f());
  }

  public void setProjection(int width, int height) {
    this.projection = new Matrix4f().setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f);
  }
}

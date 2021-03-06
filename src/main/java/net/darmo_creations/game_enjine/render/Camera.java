package net.darmo_creations.game_enjine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
  @SuppressWarnings("FieldMayBeFinal")
  private Vector3f position;
  private Matrix4f projection;

  public Camera() {
    this.position = new Vector3f(0);
  }

  public void setProjection(int width, int height) {
    this.projection = new Matrix4f().setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f);
  }

  public void setPosition(final Vector3f position) {
    this.position.set(position);
  }

  public void addPosition(final Vector3f position) {
    this.position.add(position);
  }

  public Vector3f getPosition() {
    return this.position;
  }

  public Matrix4f getUntransformedProjection() {
    return this.projection;
  }

  public Matrix4f getProjection() {
    return this.projection.translate(this.position, new Matrix4f());
  }
}

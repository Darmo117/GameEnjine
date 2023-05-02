package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
  private final String type;
  private final Vector2f size;
  private final Vector2f position;
  private float speed;
  private Vector2i nextTile;
  private final List<ResourceIdentifier> textures;

  protected Entity(String type, final Vector2f size, final Vector2f position, final List<ResourceIdentifier> textures) {
    this.type = type;
    this.size = size;
    this.position = new Vector2f(position);
    this.textures = new ArrayList<>(textures);
  }

  public String type() {
    return this.type;
  }

  public Vector2i tilePosition() {
    return new Vector2i(this.position, RoundingMode.FLOOR);
  }

  public float speed() {
    return this.speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public Vector2f position() {
    return new Vector2f(this.position);
  }

  public void setPosition(final Vector2f position) {
    this.position.set(position);
  }

  public Vector2f size() {
    return new Vector2f(this.size);
  }

  public void setSize(final Vector2f size) {
    this.size.set(size);
  }

  public ResourceIdentifier texture() {
    return this.textures.get(0); // TODO make depend on entity’s state
  }

  public void update() {
    if (!this.nextTile.equals(this.tilePosition())) {
      this.move();
    }
  }

  public void move() {
    if (this.nextTile.equals(this.tilePosition())) {
      this.position.set(this.nextTile.x, this.nextTile.y);
    } else {
      Vector2f speedV = new Vector2f();
      if (this.nextTile.x < this.position.x) {
        speedV.x = this.speed;
      } else if (this.nextTile.x > this.position.x) {
        speedV.x = -this.speed;
      }
      if (this.nextTile.y < this.position.y) {
        speedV.y = this.speed;
      } else if (this.nextTile.y > this.position.y) {
        speedV.y = -this.speed;
      }
      this.position.add(speedV);
    }
  }
}

package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import net.darmo_creations.game_enjine.world.World;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
  private static final Vector2i UP = new Vector2i(0, 1);
  private static final Vector2i DOWN = new Vector2i(0, -1);
  private static final Vector2i RIGHT = new Vector2i(1, 0);
  private static final Vector2i LEFT = new Vector2i(-1, 0);

  private static final float EPS = 1e-5f;

  private final World world;
  private final String type;
  private final Vector2f size;
  private final Vector2f position;
  private float speed;
  private Vector2i nextTile;
  private float remainingDistance;
  private final List<ResourceIdentifier> textures;

  protected Entity(World world, String type, final Vector2f size, final Vector2f position,
                   final List<ResourceIdentifier> textures) {
    this.world = world;
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
    return this.textures.get(0); // TODO make function of entity’s state
  }

  public boolean isMoving() {
    return this.nextTile != null;
  }

  public void update() {
    if (this.nextTile != null) {
      this.move();
    }
  }

  protected void move() {
    if (this.nextTile == null) {
      return;
    }
    if (this.remainingDistance < EPS) {
      this.position.set(this.nextTile);
      this.nextTile = null;
      this.remainingDistance = 0;
    } else {
      Vector2f speed = new Vector2f();
      if (this.nextTile.x < this.position.x) {
        speed.x = -this.speed;
      } else if (this.nextTile.x > this.position.x) {
        speed.x = this.speed;
      }
      if (this.nextTile.y < this.position.y) {
        speed.y = -this.speed;
      } else if (this.nextTile.y > this.position.y) {
        speed.y = this.speed;
      }
      this.remainingDistance -= speed.length();
      this.position.add(speed);
    }
  }

  public void goUp() {
    this.moveToNextTile(UP);
  }

  public void goDown() {
    this.moveToNextTile(DOWN);
  }

  public void goRight() {
    this.moveToNextTile(RIGHT);
  }

  public void goLeft() {
    this.moveToNextTile(LEFT);
  }

  private void moveToNextTile(final Vector2i direction) {
    if (direction.x() != 0 && direction.y() != 0) {
      throw new IllegalArgumentException("entities cannot move along two axes at the same time");
    }
    if (this.isMoving()) {
      return;
    }
    Vector2i pos = this.tilePosition().add(direction, new Vector2i());
    boolean canGoToTile = this.world.getTileInteraction(pos.x, pos.y)
        .map(ti -> ti.canEntityGoThrough(this.world, this))
        .orElse(false);
    if (canGoToTile) {
      this.nextTile = pos;
      this.remainingDistance = (float) this.nextTile.distance(this.tilePosition());
    }
  }
}

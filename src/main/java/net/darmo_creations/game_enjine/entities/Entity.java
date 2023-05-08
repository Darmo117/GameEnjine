package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.render.Model;
import net.darmo_creations.game_enjine.render.QuadModel;
import net.darmo_creations.game_enjine.world.Level;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class Entity {
  private static final Vector2i[] DIRECTIONS = {
      new Vector2i(0, -1), // down
      new Vector2i(-1, 0), // left
      new Vector2i(1, 0), // right
      new Vector2i(0, 1), // up
  };

  private static final float EPS = 1e-5f;

  private final Level level;
  private final String type;
  private final Vector2f size;
  private final Vector2f position;
  private final String spritesheet;
  private int direction;
  private float speed;
  private Vector2i nextTile;
  private float remainingDistance;
  private final Model model;

  protected Entity(Level level, String type, final Vector2f size, final Vector2f position, String spritesheet) {
    this.level = level;
    this.type = type;
    this.size = size;
    this.position = new Vector2f(position);
    this.direction = 0;
    this.spritesheet = spritesheet;
    this.model = new QuadModel(size, new float[] {
        0, 0,
        1, 0,
        1, 1,
        0, 1,
    });
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

  public int direction() {
    return this.direction;
  }

  public Vector2f size() {
    return new Vector2f(this.size);
  }

  public void setSize(final Vector2f size) {
    this.size.set(size);
  }

  public String spritesheet() {
    return this.spritesheet;
  }

  public Model model() {
    return this.model;
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
    this.moveToNextTile(3);
  }

  public void goDown() {
    this.moveToNextTile(0);
  }

  public void goRight() {
    this.moveToNextTile(2);
  }

  public void goLeft() {
    this.moveToNextTile(1);
  }

  private void moveToNextTile(int d) {
    Vector2i direction = DIRECTIONS[d];
    if (direction.x() != 0 && direction.y() != 0) {
      throw new IllegalArgumentException("entities cannot move along two axes at the same time");
    }
    if (this.isMoving()) {
      return;
    }
    Vector2i pos = this.tilePosition().add(direction, new Vector2i());
    boolean canGoToTile = this.level.getTileInteraction(pos.x, pos.y)
        .map(ti -> ti.canEntityGoThrough(this.level, this))
        .orElse(false);
    if (canGoToTile) {
      this.nextTile = pos;
      this.remainingDistance = (float) this.nextTile.distance(this.tilePosition());
      this.direction = d;
    }
  }
}

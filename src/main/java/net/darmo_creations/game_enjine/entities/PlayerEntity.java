package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.world.Level;
import org.joml.Vector2f;

public class PlayerEntity extends Entity {
  public PlayerEntity(Level level, final Vector2f size, final Vector2f position, String spritesheet) {
    super(level, "player", size, position, spritesheet);
  }
}

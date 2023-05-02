package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import org.joml.Vector2f;

import java.util.List;

public class PlayerEntity extends Entity {
  public PlayerEntity(final Vector2f size, final Vector2f position, final List<ResourceIdentifier> texture) {
    super("player", size, position, texture);
  }
}

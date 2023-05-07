package net.darmo_creations.game_enjine.entities;

import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import net.darmo_creations.game_enjine.world.World;
import org.joml.Vector2f;

import java.util.List;

public class PlayerEntity extends Entity {
  public PlayerEntity(World world, final Vector2f size, final Vector2f position, final List<ResourceIdentifier> texture) {
    super(world, "player", size, position, texture);
  }
}

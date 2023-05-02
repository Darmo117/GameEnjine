package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.World;

public interface TileInteraction {
  default void onEntityCollision(World world, Entity entity) {
  }

  boolean canEntityWalkInsideTile(final World world, final Entity entity);
}

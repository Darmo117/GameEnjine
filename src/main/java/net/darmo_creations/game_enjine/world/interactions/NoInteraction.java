package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.World;

public class NoInteraction implements TileInteraction {
  private static final TileInteraction INSTANCE = new NoInteraction();

  public static TileInteraction get() {
    return INSTANCE;
  }

  private NoInteraction() {
  }

  @Override
  public boolean canEntityGoThrough(World world, Entity entity) {
    return true;
  }
}

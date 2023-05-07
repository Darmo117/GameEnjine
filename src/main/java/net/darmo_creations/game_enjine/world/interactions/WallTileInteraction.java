package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.World;

public class WallTileInteraction implements TileInteraction {
  private static final WallTileInteraction INSTANCE = new WallTileInteraction();

  public static WallTileInteraction get() {
    return INSTANCE;
  }

  private WallTileInteraction() {
  }

  @Override
  public boolean canEntityGoThrough(final World world, final Entity entity) {
    return false;
  }
}

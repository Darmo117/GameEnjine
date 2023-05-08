package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.Level;

public class WallTileInteraction implements TileInteraction {
  private static final WallTileInteraction INSTANCE = new WallTileInteraction();

  public static WallTileInteraction get() {
    return INSTANCE;
  }

  private WallTileInteraction() {
  }

  @Override
  public byte id() {
    return 1;
  }

  @Override
  public boolean canEntityGoThrough(final Level level, final Entity entity) {
    return false;
  }
}

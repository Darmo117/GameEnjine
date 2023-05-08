package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.Level;

public class NoInteraction implements TileInteraction {
  private static final TileInteraction INSTANCE = new NoInteraction();

  public static TileInteraction get() {
    return INSTANCE;
  }

  private NoInteraction() {
  }

  @Override
  public byte id() {
    return 0;
  }

  @Override
  public boolean canEntityGoThrough(Level level, Entity entity) {
    return true;
  }
}

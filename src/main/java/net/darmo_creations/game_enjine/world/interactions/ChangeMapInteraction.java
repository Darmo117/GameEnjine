package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.World;

public class ChangeMapInteraction implements TileInteraction {
  private final String targetMapName;
  private final int x, y;

  public ChangeMapInteraction(final String targetMapName, int x, int y) {
    this.targetMapName = targetMapName;
    this.x = x;
    this.y = y;
  }

  @Override
  public void onEntityCollision(World world, Entity entity) {
    // TODO load map
  }

  @Override
  public boolean canEntityWalkInsideTile(World world, Entity entity) {
    return true;
  }
}

package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.World;

@SuppressWarnings("ClassCanBeRecord")
public class HurtEntityInteraction implements TileInteraction {
  private final double damageAmount;

  public HurtEntityInteraction(final double damageAmount) {
    this.damageAmount = damageAmount;
  }

  public double getDamageAmount() {
    return this.damageAmount;
  }

  @Override
  public void onEntityCollision(World world, Entity entity) {
    // TODO
  }

  @Override
  public boolean canEntityWalkInsideTile(World world, Entity entity) {
    return true;
  }
}

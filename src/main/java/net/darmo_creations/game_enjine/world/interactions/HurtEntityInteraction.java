package net.darmo_creations.game_enjine.world.interactions;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.Level;

@SuppressWarnings("ClassCanBeRecord")
public class HurtEntityInteraction implements TileInteraction {
  private final double damageAmount;

  public HurtEntityInteraction(double damageAmount) {
    this.damageAmount = damageAmount;
  }

  @Override
  public byte id() {
    return 3;
  }

  public double damageAmount() {
    return this.damageAmount;
  }

  @Override
  public void onEntityCollision(Level level, Entity entity) {
    // TODO
  }

  @Override
  public boolean canEntityGoThrough(Level level, Entity entity) {
    return true;
  }

  @Override
  public void toBuffer(ByteBuf bb) {
    TileInteraction.super.toBuffer(bb);
    bb.writeDouble(this.damageAmount);
  }
}

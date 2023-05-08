package net.darmo_creations.game_enjine.world.interactions;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.world.Level;

public interface TileInteraction {
  byte id();

  default void onEntityCollision(Level level, Entity entity) {
  }

  boolean canEntityGoThrough(final Level level, final Entity entity);

  default void toBuffer(ByteBuf bb) {
    bb.writeByte(this.id());
  }
}

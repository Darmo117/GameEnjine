package net.darmo_creations.game_enjine.world.interactions;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.entities.PlayerEntity;
import net.darmo_creations.game_enjine.utils.ByteBufferUtils;
import net.darmo_creations.game_enjine.world.Level;

public class ChangeMapInteraction implements TileInteraction {
  private final String targetMapName;
  private final int doorID;
  private final State state;

  public ChangeMapInteraction(String targetMapName, int doorID, State state) {
    this.targetMapName = targetMapName;
    this.doorID = doorID;
    this.state = state;
  }

  @Override
  public byte id() {
    return 2;
  }

  @Override
  public void onEntityCollision(Level level, Entity entity) {
    if (this.state.isOpen() && entity instanceof PlayerEntity) {
      // TODO load map
    }
  }

  @Override
  public boolean canEntityGoThrough(Level level, Entity entity) {
    return this.state.isOpen();
  }

  @Override
  public void toBuffer(ByteBuf bb) {
    TileInteraction.super.toBuffer(bb);
    ByteBufferUtils.putString(bb, this.targetMapName);
    bb.writeInt(this.doorID);
    ByteBufferUtils.putEnumValue(bb, this.state);
  }

  public enum State {
    OPEN, CLOSED, LOCKED;

    public boolean isOpen() {
      return this == OPEN;
    }

    public boolean isClosed() {
      return this == CLOSED || this.isLocked();
    }

    public boolean isLocked() {
      return this == LOCKED;
    }
  }
}

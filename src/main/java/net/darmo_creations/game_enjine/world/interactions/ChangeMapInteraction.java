package net.darmo_creations.game_enjine.world.interactions;

import net.darmo_creations.game_enjine.entities.Entity;
import net.darmo_creations.game_enjine.entities.PlayerEntity;
import net.darmo_creations.game_enjine.world.World;

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
  public void onEntityCollision(World world, Entity entity) {
    if (this.state.isOpen() && entity instanceof PlayerEntity) {
      // TODO load map
    }
  }

  @Override
  public boolean canEntityGoThrough(World world, Entity entity) {
    return this.state.isOpen();
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

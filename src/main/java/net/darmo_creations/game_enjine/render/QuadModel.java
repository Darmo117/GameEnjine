package net.darmo_creations.game_enjine.render;

import org.joml.Vector2f;

public class QuadModel extends Model {
  public QuadModel(final Vector2f size, float[] texture) {
    super(
        new float[] {
            0, size.y(), 0,         // TOP LEFT 0
            size.x(), size.y(), 0,  // TOP RIGHT 1
            size.x(), 0, 0,         // BOTTOM RIGHT 2
            0, 0, 0,                // BOTTOM LEFT 3
        },
        texture,
        new int[] {
            0, 1, 2,
            2, 3, 0,
        }
    );
  }
}

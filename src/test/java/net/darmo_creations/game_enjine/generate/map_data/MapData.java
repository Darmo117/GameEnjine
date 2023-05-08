package net.darmo_creations.game_enjine.generate.map_data;

import net.darmo_creations.game_enjine.utils.Color;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import org.joml.Vector2i;

import java.util.Arrays;

public abstract class MapData {
  private final Vector2i size;
  private final Vector2i defaultStartLocation;
  private final Color bgColor;
  private final byte entityLayer;

  public MapData(Vector2i size, Vector2i defaultStartLocation, Color bgColor, byte entityLayer) {
    this.size = size;
    this.defaultStartLocation = defaultStartLocation;
    this.bgColor = bgColor;
    this.entityLayer = entityLayer;
  }

  public Vector2i size() {
    return this.size;
  }

  public Vector2i defaultStartLocation() {
    return this.defaultStartLocation;
  }

  public Color bgColor() {
    return this.bgColor;
  }

  public byte entityLayer() {
    return this.entityLayer;
  }

  public abstract TileData[][][] layers();

  public abstract TileInteraction[][] interactions();

  protected static <T> T[] fill(T[] array, T value) {
    Arrays.fill(array, value);
    return array;
  }

  public record TileData(short tilesetID, short tileID) {
    public static TileData create(int tilesetID, int tileID) {
      return new TileData((short) tilesetID, (short) tileID);
    }
  }
}

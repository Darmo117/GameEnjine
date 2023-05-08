package net.darmo_creations.game_enjine.world;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.utils.ByteBufferUtils;
import net.darmo_creations.game_enjine.world.interactions.*;
import org.joml.Vector2i;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class LevelLoader {
  public static final String MAPS_DIR = "maps";

  private final GameEnjine engine;

  public LevelLoader(GameEnjine engine) {
    this.engine = engine;
  }

  public Level loadLevel(String name) throws IOException {
    File file = new File(new File(GameEnjine.DATA_DIR_PATH, MAPS_DIR), name + ".map");
    ByteBuf bb = this.readFile(file);
    //noinspection unused
    int version = bb.readInt(); // Future-proofing
    int width = bb.readShort();
    int height = bb.readShort();
    int defaultSpawnX = bb.readShort();
    int defaultSpawnY = bb.readShort();
    int bgColor = bb.readInt();
    int entityLayer = bb.readByte();
    Tile[][][] tiles = this.loadTiles(bb, width, height);
    TileInteraction[][] interactions = this.loadInteractions(bb, width, height);
    return new Level(
        this.engine,
        name,
        width,
        height,
        new Vector2i(defaultSpawnX, defaultSpawnY),
        tiles,
        interactions,
        entityLayer,
        bgColor
    );
  }

  private ByteBuf readFile(final File file) throws IOException {
    ByteBuf bb = Unpooled.buffer();

    try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(file))) {
      byte[] data = new byte[1024];
      while (gzis.available() != 0) {
        int bytesRead = gzis.read(data);
        if (bytesRead > 0) {
          bb.writeBytes(data, 0, bytesRead);
        }
      }
    }

    return bb;
  }

  private Tile[][][] loadTiles(ByteBuf bb, int width, int height) {
    int layersNb = bb.readByte();
    Tile[][][] tiles = new Tile[layersNb][height][width];
    for (int layer = 0; layer < layersNb; layer++) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int tilesetID = bb.readShort();
          int tileID = bb.readShort();
          if (tilesetID >= 0) {
            tiles[layer][y][x] = new Tile(tilesetID, tileID);
          }
        }
      }
    }
    return tiles;
  }

  private TileInteraction[][] loadInteractions(ByteBuf bb, int width, int height) {
    TileInteraction[][] interactions = new TileInteraction[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        interactions[y][x] = switch (bb.readByte()) {
          case 1 -> WallTileInteraction.get();
          case 2 -> new ChangeMapInteraction(
              ByteBufferUtils.getString(bb),
              bb.readInt(),
              ByteBufferUtils.getEnumValue(bb, ChangeMapInteraction.State.class)
          );
          case 3 -> new HurtEntityInteraction(bb.readDouble());
          default -> NoInteraction.get();
        };
      }
    }
    return interactions;
  }
}

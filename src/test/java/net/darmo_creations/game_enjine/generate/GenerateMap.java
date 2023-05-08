package net.darmo_creations.game_enjine.generate;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.darmo_creations.game_enjine.generate.map_data.MapData;
import net.darmo_creations.game_enjine.generate.map_data.NoWallsMapData;
import net.darmo_creations.game_enjine.generate.map_data.TestMapData;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class GenerateMap {
  public static final int VERSION = 1;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: GenerateMap <mapID: String>");
      System.exit(1);
    }
    String mapID = args[0];
    MapData data = switch (mapID) {
      case "test" -> new TestMapData();
      case "no_walls" -> new NoWallsMapData();
      default -> throw new IllegalArgumentException("undefined map ID: " + mapID);
    };

    ByteBuf bb = Unpooled.buffer();
    bb.writeInt(VERSION);
    short width = (short) data.size().x();
    bb.writeShort(width);
    short height = (short) data.size().y();
    bb.writeShort(height);
    bb.writeShort(data.defaultStartLocation().x());
    bb.writeShort(data.defaultStartLocation().y());
    bb.writeInt(data.bgColor().rgb());
    bb.writeByte(data.entityLayer());
    putLayers(bb, data.layers(), width, height);
    putInteractions(bb, data.interactions(), width, height);

    File file = Paths.get("data", "maps", mapID + ".map").toFile();
    try (GZIPOutputStream gzout = new GZIPOutputStream(new FileOutputStream(file))) {
      gzout.write(bb.array());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void putLayers(ByteBuf bb, final MapData.TileData[][][] layers, short width, short height) {
    byte layersNb = (byte) layers.length;
    bb.writeByte(layersNb);
    for (int layer = 0; layer < layersNb; layer++) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          MapData.TileData tileData = layers[layer][y][x];
          bb.writeShort(tileData.tilesetID());
          bb.writeShort(tileData.tileID());
        }
      }
    }
  }

  private static void putInteractions(ByteBuf bb, final TileInteraction[][] interactions, short width, short height) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        interactions[y][x].toBuffer(bb);
      }
    }
  }
}

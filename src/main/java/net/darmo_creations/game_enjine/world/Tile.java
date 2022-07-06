package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.render.Texture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tile {
  public static final String TILES_DIR = "tiles";

  public static final Map<Integer, Tile> TILES = new HashMap<>();
  private static int globalID = 0;

  public static void loadTiles() {
    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get(GameEnjine.DATA_DIR_PATH, WorldLoader.MAPS_DIR, "tiles.index"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    for (String line : lines) {
      try {
        String[] values = line.split(":", 2);
        int id = Integer.parseInt(values[0]);
        if (TILES.containsKey(id)) {
          throw new IllegalStateException("duplicate tile ID %d".formatted(id));
        }
        TILES.put(id, new Tile(values[1]));
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private final int id;
  private final String texture;

  private Tile(final String texture) {
    this.id = globalID++;
    this.texture = Paths.get(GameEnjine.DATA_DIR_PATH, Texture.TEXTURES_DIR, TILES_DIR, texture).toString();
  }

  public int getID() {
    return this.id;
  }

  public String getTexture() {
    return this.texture;
  }
}

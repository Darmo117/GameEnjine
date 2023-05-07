package net.darmo_creations.game_enjine.world;

import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.utils.Triplet;
import net.darmo_creations.game_enjine.world.interactions.ChangeMapInteraction;
import net.darmo_creations.game_enjine.world.interactions.HurtEntityInteraction;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import net.darmo_creations.game_enjine.world.interactions.WallTileInteraction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldLoader {
  public static final String MAPS_DIR = "maps";

  public World loadWorld(String name) throws IOException {
    Triplet<Integer, Integer, Tile[]> triplet = this.loadTiles(name);
    int width = triplet.left();
    int height = triplet.middle();
    Tile[] tiles = triplet.right();
    TileInteraction[] interactions = this.loadInteractions(name, width, height);
    return new World(name, width, height, tiles, interactions);
  }

  private Triplet<Integer, Integer, Tile[]> loadTiles(String mapName) throws IOException {
    File path = new File(GameEnjine.DATA_DIR_PATH, MAPS_DIR);
    List<String> lines = Files.readAllLines(new File(path, mapName + ".tiles").toPath());
    int width, height;
    Tile[] tiles;
    try {
      String[] rawMapSize = lines.get(0).trim().split(" ", 2);
      if (rawMapSize.length != 2) {
        throw new IOException("could not parse %s.tiles file".formatted(mapName));
      }
      width = Integer.parseInt(rawMapSize[0]);
      height = Integer.parseInt(rawMapSize[1]);
      tiles = new Tile[width * height];
      for (int row = 0; row < height; row++) {
        String line = lines.get(row + 1).trim();
        String[] tileIDs = line.split(" ", width);
        if (tileIDs.length != width) {
          throw new IOException("could not parse %s.tiles file".formatted(mapName));
        }
        System.arraycopy(Arrays.stream(tileIDs).map(id -> Tile.TILES.get(Integer.parseInt(id))).toArray(Tile[]::new), 0, tiles, row * width, width);
      }
    } catch (IndexOutOfBoundsException | NumberFormatException e) {
      throw new IOException("could not parse %s.tiles file".formatted(mapName), e);
    }
    return new Triplet<>(width, height, tiles);
  }

  private TileInteraction[] loadInteractions(String mapName, int width, int height) throws IOException {
    File path = new File(GameEnjine.DATA_DIR_PATH, MAPS_DIR);
    List<String> lines = Files.readAllLines(new File(path, mapName + ".int").toPath());
    int[] ints = new int[width * height];
    try {
      for (int row = 0; row < height; row++) {
        String line = lines.get(row).trim();
        String[] interactionIDs = line.split(" ", width);
        if (interactionIDs.length != width) {
          throw new IOException("could not parse %s.int file".formatted(mapName));
        }
        System.arraycopy(Arrays.stream(interactionIDs).mapToInt(Integer::parseInt).toArray(), 0, ints, row * width, width);
      }
    } catch (IndexOutOfBoundsException | NumberFormatException e) {
      throw new IOException("could not parse %s.int file".formatted(mapName), e);
    }
    Map<Integer, TileInteraction> interactionsCache = new HashMap<>();
    try {
      for (int i = height; i < lines.size(); i++) {
        String[] line = lines.get(i).trim().split(":", 2);
        interactionsCache.put(Integer.parseInt(line[0]), this.getInteraction(line[1]));
      }
    } catch (IndexOutOfBoundsException | NumberFormatException e) {
      throw new IOException("could not parse %s.int file".formatted(mapName), e);
    }
    return Arrays.stream(ints).mapToObj(i -> interactionsCache.getOrDefault(i, null)).toArray(TileInteraction[]::new);
  }

  private TileInteraction getInteraction(String pattern) {
    if ("wall".equals(pattern)) {
      return WallTileInteraction.get();
    }
    Matcher matcher = Pattern.compile("^(\\w+)\\[(\\w+=[^=\\[\\],]+(?:,\\w+=[^=\\[\\],]+)*)]$").matcher(pattern);
    if (matcher.find()) {
      Map<String, String> args = this.extractArgs(matcher.group(2));
      return switch (matcher.group(1)) {
        case "change_map" -> {
          String[] location = args.get("location").split(";", 2);
          int x = Integer.parseInt(location[0]);
          int y = Integer.parseInt(location[1]);
          yield new ChangeMapInteraction(args.get("target"), x, y);
        }
        case "hurt" -> new HurtEntityInteraction(Double.parseDouble(args.get("damage")));
        default -> null;
      };
    }
    return null;
  }

  private Map<String, String> extractArgs(String pattern) {
    Map<String, String> args = new HashMap<>();
    String[] entries = pattern.split(",");
    for (String entry : entries) {
      String[] values = entry.split("=", 2);
      args.put(values[0], values[1]);
    }
    return args;
  }
}

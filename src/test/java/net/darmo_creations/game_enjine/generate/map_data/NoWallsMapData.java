package net.darmo_creations.game_enjine.generate.map_data;

import net.darmo_creations.game_enjine.utils.Color;
import net.darmo_creations.game_enjine.world.interactions.ChangeMapInteraction;
import net.darmo_creations.game_enjine.world.interactions.NoInteraction;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import org.joml.Vector2i;

public class NoWallsMapData extends MapData {
  public NoWallsMapData() {
    super(new Vector2i(50), new Vector2i(0, 49), new Color(0, 0.5f, 0), (byte) 1);
  }

  @Override
  public TileData[][][] layers() {
    TileData[][] main = new TileData[50][];
    for (int i = 0; i < main.length; i++) {
      main[i] = fill(new TileData[50], TileData.create(-1, -1));
    }
    main[1][1] = TileData.create(4, 2);
    return new TileData[][][] {
        fill(new TileData[50][], fill(new TileData[50], TileData.create(1, 102))),
        main,
    };
  }

  @Override
  public TileInteraction[][] interactions() {
    TileInteraction[][] main = new TileInteraction[50][];
    for (int i = 0; i < main.length; i++) {
      main[i] = fill(new TileInteraction[50], NoInteraction.get());
    }
    main[1][1] = new ChangeMapInteraction("test", 0, ChangeMapInteraction.State.OPEN);
    return main;
  }
}

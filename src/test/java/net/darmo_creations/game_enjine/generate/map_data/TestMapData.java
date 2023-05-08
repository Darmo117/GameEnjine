package net.darmo_creations.game_enjine.generate.map_data;

import net.darmo_creations.game_enjine.utils.Color;
import net.darmo_creations.game_enjine.world.interactions.ChangeMapInteraction;
import net.darmo_creations.game_enjine.world.interactions.NoInteraction;
import net.darmo_creations.game_enjine.world.interactions.TileInteraction;
import net.darmo_creations.game_enjine.world.interactions.WallTileInteraction;
import org.joml.Vector2i;

public class TestMapData extends MapData {
  public TestMapData() {
    super(new Vector2i(16), new Vector2i(2, 8), new Color(0, 0.5f, 0), (byte) 0);
  }

  @Override
  public TileData[][][] layers() {
    TileData nn = TileData.create(-1, -1);
    TileData fv = TileData.create(21, 120);
    TileData fe = TileData.create(21, 139);
    TileData fh = TileData.create(21, 141);
    TileData ft = TileData.create(21, 105);
    TileData fp = TileData.create(21, 96);
    TileData t1 = TileData.create(21, 124);
    TileData c1 = TileData.create(21, 132);
    TileData t2 = TileData.create(21, 104);
    TileData c2 = TileData.create(21, 112);
    TileData t3 = TileData.create(21, 109);
    TileData c3 = TileData.create(21, 117);
    TileData c4 = TileData.create(21, 118);
    TileData pp = TileData.create(4, 80);
    TileData ps = TileData.create(4, 82);

    TileData[] empty = fill(new TileData[16], nn);
    TileData[][] bg = new TileData[16][];
    for (int i = 0; i < bg.length; i++) {
      bg[i] = fill(new TileData[16], TileData.create(21, 56));
    }
    bg[0][2] = TileData.create(4, 18);
    return new TileData[][][] {
        bg,
        new TileData[][] {
            new TileData[] {nn, fp, nn, fp, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, t1, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, c1, fh, fh, fh, c3, nn, c4, fh, fh, fh, fh, c3, nn},
            new TileData[] {nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, c2, fh, fh, fh, c3, nn, c4, fh, fh, fh, fh, c3, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fv, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, fe, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
            new TileData[] {nn, nn, nn, fv, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn, nn},
        },
        new TileData[][] {
            empty,
            empty,
            fill(new TileData[16], TileData.create(4, 65)),
            new TileData[] {pp, ps, ps, ps, pp, ps, ps, ps, pp, ps, ps, ps, pp, ps, ps, ps},
            empty,
            empty,
            new TileData[] {nn, nn, nn, nn, ft, ft, ft, t3, nn, t2, ft, ft, ft, ft, t3, nn},
            empty,
            new TileData[] {nn, nn, nn, t2, ft, ft, ft, t3, nn, t2, ft, ft, ft, ft, t3, nn},
            empty,
            empty,
            empty,
            empty,
            empty,
            empty,
            empty,
        },
    };
  }

  @Override
  public TileInteraction[][] interactions() {
    TileInteraction no = NoInteraction.get();
    TileInteraction wa = WallTileInteraction.get();
    TileInteraction cm = new ChangeMapInteraction("no_walls", 0, ChangeMapInteraction.State.OPEN);
    return new TileInteraction[][] {
        new TileInteraction[] {no, no, cm, no, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {wa, wa, no, wa, wa, no, no, no, wa, no, no, no, wa, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, wa, wa, wa, wa, no, wa, wa, wa, wa, wa, wa, no},
        new TileInteraction[] {no, wa, no, no, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, wa, wa, wa, wa, no, wa, wa, wa, wa, wa, wa, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, wa, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
        new TileInteraction[] {no, no, no, wa, no, no, no, no, no, no, no, no, no, no, no, no},
    };
  }
}

package net.darmo_creations.game_enjine.world;

import java.io.File;

public class Tile {
  public static final Tile[] TILES = new Tile[255];
  private static int globalID = 0;

  public static final Tile TEST_TILE = new Tile("debug");
  public static final Tile TEST_TILE_2 = new Tile("dirt").setSolid();

  private int id;
  private boolean solid;
  private String texture;

  public Tile(final String texture) {
    this.id = globalID++;
    this.texture = new File("data/textures/tiles", texture).toString();
    this.solid = false;
    TILES[this.id] = this;
  }

  public Tile setSolid() {
    this.solid = true;
    return this;
  }

  public boolean isSolid() {
    return this.solid;
  }

  public int getID() {
    return this.id;
  }

  public String getTexture() {
    return this.texture;
  }
}

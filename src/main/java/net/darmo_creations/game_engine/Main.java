package net.darmo_creations.game_engine;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    GameEnjine gameEngine;
    try {
      gameEngine = new GameEnjine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    gameEngine.run();
  }
}

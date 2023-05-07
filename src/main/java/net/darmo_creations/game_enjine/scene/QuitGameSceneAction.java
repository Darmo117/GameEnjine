package net.darmo_creations.game_enjine.scene;

import net.darmo_creations.game_enjine.GameEnjine;

public class QuitGameSceneAction implements SceneAction {
  @Override
  public void execute(GameEnjine engine) {
    engine.window().setShouldClose();
  }
}

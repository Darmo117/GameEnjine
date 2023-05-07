package net.darmo_creations.game_enjine.scene;

import net.darmo_creations.game_enjine.GameEnjine;

public class ChangeSceneAction implements SceneAction {
  private final Scene scene;

  public ChangeSceneAction(final Scene toScene) {
    this.scene = toScene;
  }

  @Override
  public void execute(GameEnjine engine) {
    engine.transitionToScene(this.scene);
  }
}

package net.darmo_creations.game_enjine.scene;

import net.darmo_creations.game_enjine.render.Shader;
import net.darmo_creations.game_enjine.render.Window;

import java.util.Optional;

public abstract class Scene {
  private final String name;

  protected Scene(String name) {
    this.name = name;
  }

  public String name() {
    return this.name;
  }

  public abstract Optional<SceneAction> update(final Window window);

  public abstract void onWindowResized(final Window window);

  public abstract void render(Shader shader, final Window window);
}

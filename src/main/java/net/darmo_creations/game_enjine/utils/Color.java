package net.darmo_creations.game_enjine.utils;

public record Color(float r, float g, float b) {
  public Color(int rgb) {
    this(
        (rgb >> 16 & 255) / 255f,
        (rgb >> 8 & 255) / 255f,
        (rgb & 255) / 255f
    );
  }

  public int rgb() {
    return (int) (this.r * 255) << 16 | (int) (this.g * 255) << 8 | (int) (this.b * 255);
  }
}

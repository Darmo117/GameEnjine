package net.darmo_creations.game_enjine.render;

import net.darmo_creations.game_enjine.utils.TimeUtils;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;

public class Texture {
  public static final Texture MISSING_TEXTURE;

  static {
    int width, height;
    width = height = 4; // Actual is not relevant, must be pair
    ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int r = 0, g = 0, b = 0;
        if (i < width / 2 && j < height / 2 || i >= width / 2 && j >= height / 2) {
          r = b = 255;
        }
        pixels.put((byte) r);
        pixels.put((byte) g);
        pixels.put((byte) b);
        pixels.put((byte) 255);
      }
    }
    MISSING_TEXTURE = new Texture(pixels, width, height);
  }

  private final int[] ids;
  private int framePointer;
  private final double frameInterval;
  private double lastTime;
  private double elapsedTime;

  public Texture(final ByteBuffer pixels, int width, int height) {
    this(new ByteBuffer[] {pixels}, width, height, 0);
  }

  public Texture(final ByteBuffer[] pixels, int width, int height, double frameDelay) {
    this.framePointer = 0;
    this.lastTime = TimeUtils.getTime();
    this.frameInterval = frameDelay;
    this.ids = new int[pixels.length];
    for (int i = 0; i < this.ids.length; i++) {
      pixels[i].flip();
      this.ids[i] = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, this.ids[i]);
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels[i]);
    }
  }

  public void bind(int sampler) {
    if (sampler < 0 || sampler > 31) {
      throw new IllegalArgumentException("sampler must be in [0, 31]");
    }
    glActiveTexture(GL_TEXTURE0 + sampler);
    int framesNumber = this.ids.length;
    if (framesNumber > 1) {
      double currentTime = TimeUtils.getTime();
      this.elapsedTime += currentTime - this.lastTime;
      if (this.elapsedTime >= this.frameInterval) {
        this.elapsedTime = 0;
        this.framePointer = (this.framePointer + 1) % framesNumber;
      }
      this.lastTime = currentTime;
    }
    glBindTexture(GL_TEXTURE_2D, this.ids[this.framePointer]);
  }
}

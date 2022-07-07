package net.darmo_creations.game_enjine.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;

public class Texture {
  public static final String TEXTURES_DIR = "textures";

  private static final int DEFAULT_TEXTURE_SIZE = 2; // Actual size doesn’t matter but must be even
  private static final ByteBuffer DEFAULT_TEXTURE;

  static {
    int width, height;
    width = height = DEFAULT_TEXTURE_SIZE;
    DEFAULT_TEXTURE = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int r = 0, g = 0, b = 0;
        if (i < width / 2 && j < height / 2 || i >= width / 2 && j >= height / 2) {
          r = b = 255;
        }
        DEFAULT_TEXTURE.put((byte) r);
        DEFAULT_TEXTURE.put((byte) g);
        DEFAULT_TEXTURE.put((byte) b);
        DEFAULT_TEXTURE.put((byte) 255);
      }
    }
  }

  private final int id;

  public Texture(final String path) {
    BufferedImage image;
    try {
      image = ImageIO.read(new File(path));
    } catch (IOException e) {
      image = null;
    }
    int width, height;
    ByteBuffer pixels;
    if (image != null) {
      width = image.getWidth();
      height = image.getHeight();
      pixels = this.extractPixels(image, width, height);
    } else {
      width = height = DEFAULT_TEXTURE_SIZE;
      pixels = DEFAULT_TEXTURE;
    }
    pixels.flip();
    this.id = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, this.id);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
  }

  /**
   * Extracts pixel data from the given image and puts it into a byte buffer.
   */
  private ByteBuffer extractPixels(BufferedImage image, int width, int height) {
    ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
    int[] pixelData = image.getRGB(0, 0, width, height, null, 0, width);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int pixel = pixelData[i * width + j];
        pixels.put((byte) ((pixel >> 16) & 0xff));
        pixels.put((byte) ((pixel >> 8) & 0xff));
        pixels.put((byte) (pixel & 0xff));
        pixels.put((byte) ((pixel >> 24) & 0xff));
      }
    }
    return pixels;
  }

  public void bind(final int sampler) {
    if (sampler < 0 || sampler > 31) {
      throw new IllegalArgumentException("sampler must be in [0, 31]");
    }
    glActiveTexture(GL_TEXTURE0 + sampler);
    glBindTexture(GL_TEXTURE_2D, this.id);
  }
}

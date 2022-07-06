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

  private final int id;

  public Texture(final String path) throws IOException {
    BufferedImage image = ImageIO.read(new File(path));
    int width = image.getWidth();
    int height = image.getHeight();
    int[] pixelData = image.getRGB(0, 0, width, height, null, 0, width);
    ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int pixel = pixelData[i * width + j];
        pixels.put((byte) ((pixel >> 16) & 0xff));
        pixels.put((byte) ((pixel >> 8) & 0xff));
        pixels.put((byte) (pixel & 0xff));
        pixels.put((byte) ((pixel >> 24) & 0xff));
      }
    }
    pixels.flip();
    this.id = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, this.id);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
  }

  public void bind(final int sampler) {
    if (sampler < 0 || sampler > 31) {
      throw new IllegalArgumentException("sampler must be in [0, 31]");
    }
    glActiveTexture(GL_TEXTURE0 + sampler);
    glBindTexture(GL_TEXTURE_2D, this.id);
  }
}

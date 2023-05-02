package net.darmo_creations.game_enjine.render;

import net.darmo_creations.game_enjine.utils.ResourceIdentifier;
import net.darmo_creations.game_enjine.utils.TimeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
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

  private final int[] ids;
  private final AnimationData animationData;
  private int framePointer;
  private double lastTime;
  private double elapsedTime;

  public Texture(ResourceIdentifier identifier) {
    this.framePointer = 0;
    this.lastTime = TimeUtils.getTime();
    this.animationData = this.loadAnimationData(identifier);
    this.ids = new int[this.animationData.framesNumber()];
    for (int i = 0; i < this.ids.length; i++) {
      BufferedImage image;
      try {
        String fileName = identifier.fullPath() + (this.ids.length > 1 ? "_" + i : "") + ".png";
        image = ImageIO.read(new File(fileName));
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
      this.ids[i] = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, this.ids[i]);
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }
  }

  private AnimationData loadAnimationData(ResourceIdentifier texture) {
    JSONParser parser = new JSONParser();
    int frames;
    double baseInterval;
    try (FileReader reader = new FileReader(texture.fullPath() + ".anim")) {
      JSONObject root = (JSONObject) parser.parse(reader);
      frames = (int) root.get("frames");
      baseInterval = (double) root.get("base_interval");
    } catch (IOException | ParseException | ClassCastException e) {
      return new AnimationData(1, 0);
    }
    return new AnimationData(frames, baseInterval);
  }

  /**
   * Extracts pixel data from the given image and puts it into a byte buffer.
   */
  private ByteBuffer extractPixels(final BufferedImage image, int width, int height) {
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

  public void bind(int sampler) {
    if (sampler < 0 || sampler > 31) {
      throw new IllegalArgumentException("sampler must be in [0, 31]");
    }
    glActiveTexture(GL_TEXTURE0 + sampler);
    int framesNumber = this.animationData.framesNumber();
    if (framesNumber > 1) {
      double currentTime = TimeUtils.getTime();
      this.elapsedTime += currentTime - this.lastTime;
      if (this.elapsedTime >= this.animationData.baseInterval()) {
        this.elapsedTime = 0;
        this.framePointer = (this.framePointer + 1) % framesNumber;
      }
      this.lastTime = currentTime;
    }
    glBindTexture(GL_TEXTURE_2D, this.ids[this.framePointer]);
  }

  private record AnimationData(int framesNumber, double baseInterval) {
  }
}

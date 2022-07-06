package net.darmo_creations.game_enjine.render;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_load;

public record ByteImage(int width, int height, ByteBuffer image) {
  public static ByteImage load(final String path) throws IOException {
    ByteBuffer image;
    int width, heigh;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer comp = stack.mallocInt(1);
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);

      image = stbi_load(path, w, h, comp, 4);
      if (image == null) {
        throw new IOException("Could not load image at %s".formatted(path));
      }
      width = w.get();
      heigh = h.get();
    }
    return new ByteImage(width, heigh, image);
  }
}

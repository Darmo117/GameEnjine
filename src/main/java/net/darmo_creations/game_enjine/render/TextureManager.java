package net.darmo_creations.game_enjine.render;

import net.darmo_creations.game_enjine.GameEnjine;
import net.darmo_creations.game_enjine.utils.Logging;
import org.json.simple.JSONArray;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TextureManager {
  public static final File TEXTURES_DIR = new File(GameEnjine.DATA_DIR_PATH, "textures");
  public static final File TILESETS_DIR = new File(TEXTURES_DIR, "tilesets");
  public static final File SPRITESHEETS_DIR = new File(TEXTURES_DIR, "spritesheets");

  private final Map<Integer, Texture[]> textures = new HashMap<>();
  private final Map<String, Texture[]> spritesheets = new HashMap<>();

  private final Logger logger = Logging.getLogger("Texture manager");

  public TextureManager() {
    this.logger.info("Initializing…");
    this.loadTilesets();
    this.loadSpriteSheets();
    this.logger.info("Done");
  }

  public Texture getTexture(int tilesetID, int textureIndex) {
    Texture[] textures = this.textures.get(tilesetID);
    if (textures == null || textureIndex < 0 || textureIndex >= textures.length) {
      return Texture.MISSING_TEXTURE;
    }
    return textures[textureIndex];
  }

  public Texture[] getEntityTextures(String spritesheet) {
    if (!this.spritesheets.containsKey(spritesheet)) {
      return new Texture[] {Texture.MISSING_TEXTURE};
    }
    return this.spritesheets.get(spritesheet);
  }

  private void loadTilesets() {
    this.logger.info("Loading tilesets…");
    Object jsonData;
    try (FileReader in = new FileReader(new File(TILESETS_DIR, "tilesets.json"))) {
      jsonData = new JSONParser().parse(in);
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
    if (!(jsonData instanceof JSONArray jsonArray)) {
      throw new RuntimeException("expected JSONArray, got " + jsonData);
    }
    for (int id = 0; id < jsonArray.size(); id++) {
      Object item = jsonArray.get(id);
      if (!(item instanceof String tilesetName)) {
        throw new RuntimeException("expected String, got " + item);
      }
      try {
        this.textures.put(id, this.loadTileset(new File(TILESETS_DIR, tilesetName)));
      } catch (ArrayIndexOutOfBoundsException | NumberFormatException | IOException | ParseException e) {
        this.logger.severe("An unexpected error occured while loading tileset %s:\n%s".formatted(tilesetName, e));
        throw new RuntimeException(e);
      }
    }
  }

  private Texture[] loadTileset(final File file) throws IOException, ParseException {
    String fileName = file.getPath() + ".json";
    Object jsonData;
    try (FileReader in = new FileReader(fileName)) {
      jsonData = new JSONParser().parse(in);
    }
    if (!(jsonData instanceof JSONObject jsonObject)) {
      throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN,
          "Tileset %s: expected JSONObject, got %s".formatted(fileName, jsonData));
    }
    Object value = jsonObject.get("resolution");
    if (!(value instanceof Long resolution)) {
      throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN,
          "Tileset %s: expected Long, got %s".formatted(fileName, value));
    }
    int res = resolution.intValue();
    BufferedImage image = ImageIO.read(file);
    Texture[] textures = new Texture[image.getWidth() * image.getHeight()];
    for (int y = 0, i = 0; y < image.getHeight(); y += res) {
      for (int x = 0; x < image.getWidth(); x += res, i++) {
        textures[i] = new Texture(this.extractPixels(image, x, y, res, res), res, res);
      }
    }
    return textures;
  }

  /**
   * Extracts pixel data from the given image and puts it into a byte buffer.
   */
  private ByteBuffer extractPixels(final BufferedImage image, int x, int y, int width, int height) {
    ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
    int[] pixelData = image.getRGB(x, y, width, height, null, 0, width);
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

  private void loadSpriteSheets() {
    this.logger.info("Loading spritesheets…");
    // TODO
  }
}

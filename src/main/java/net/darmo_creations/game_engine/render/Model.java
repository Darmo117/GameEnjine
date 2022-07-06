package net.darmo_creations.game_engine.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Model {
  private final int drawCount;
  private final int verticesID;
  private final int textureID;
  private final int indicesID;

  public Model(final float[] vertices, final float[] texture, final int[] indices) {
    this.drawCount = indices.length;
    this.verticesID = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.verticesID);
    glBufferData(GL_ARRAY_BUFFER, this.createBuffer(vertices), GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    this.textureID = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.textureID);
    glBufferData(GL_ARRAY_BUFFER, this.createBuffer(texture), GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    this.indicesID = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesID);
    IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
    buffer.put(indices);
    buffer.flip();
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  private FloatBuffer createBuffer(float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  public void render() {
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

    glBindBuffer(GL_ARRAY_BUFFER, this.verticesID);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

    glBindBuffer(GL_ARRAY_BUFFER, this.textureID);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesID);
    glDrawElements(GL_TRIANGLES, this.drawCount, GL_UNSIGNED_INT, 0);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
  }
}

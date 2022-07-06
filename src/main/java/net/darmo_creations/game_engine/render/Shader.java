package net.darmo_creations.game_engine.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
  private static final String SHADERS_DIR_PATH = "/shaders";

  private final int programID;
  private final int vertexShaderID;
  private final int fragmentShaderID;

  public Shader(final String path) throws IOException {
    this.programID = glCreateProgram();

    this.vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(this.vertexShaderID, this.readFile(path + ".vs"));
    glCompileShader(this.vertexShaderID);
    if (glGetShaderi(this.vertexShaderID, GL_COMPILE_STATUS) != 1) {
      throw new IOException(glGetShaderInfoLog(this.vertexShaderID));
    }

    this.fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(this.fragmentShaderID, this.readFile(path + ".fs"));
    glCompileShader(this.fragmentShaderID);
    if (glGetShaderi(this.fragmentShaderID, GL_COMPILE_STATUS) != 1) {
      throw new IOException(glGetShaderInfoLog(this.fragmentShaderID));
    }

    glAttachShader(this.programID, this.vertexShaderID);
    glAttachShader(this.programID, this.fragmentShaderID);

    glBindAttribLocation(this.programID, 0, "vertices");
    glBindAttribLocation(this.programID, 1, "textures");

    glLinkProgram(this.programID);
    if (glGetProgrami(this.programID, GL_LINK_STATUS) != 1) {
      throw new IOException(glGetProgramInfoLog(this.programID));
    }
    glValidateProgram(this.programID);
    if (glGetProgrami(this.programID, GL_VALIDATE_STATUS) != 1) {
      throw new IOException(glGetProgramInfoLog(this.programID));
    }
  }

  public void setUniform(final String name, final int value) {
    int location = glGetUniformLocation(this.programID, name);
    if (location != -1) {
      glUniform1i(location, value);
    }
  }

  public void setUniform(final String name, final Vector4f value) {
    int location = glGetUniformLocation(this.programID, name);
    if (location != -1) {
      glUniform4f(location, value.x, value.y, value.z, value.w);
    }
  }

  public void setUniform(final String name, final Matrix4f value) {
    int location = glGetUniformLocation(this.programID, name);
    FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
    value.get(matrixData);
    if (location != -1) {
      glUniformMatrix4fv(location, false, matrixData);
    }
  }

  public void bind() {
    glUseProgram(this.programID);
  }

  private String readFile(final String fileName) throws IOException {
    StringBuilder outputString = new StringBuilder();
    URI filePath;
    try {
      filePath = Objects.requireNonNull(this.getClass().getResource(new File(SHADERS_DIR_PATH, fileName).toString())).toURI();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e); // Should never happen
    }
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        outputString.append(line);
        outputString.append("\n");
      }
    }
    return outputString.toString();
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      glDetachShader(this.programID, this.vertexShaderID);
      glDetachShader(this.programID, this.fragmentShaderID);
      glDeleteShader(this.vertexShaderID);
      glDeleteShader(this.fragmentShaderID);
      glDeleteProgram(this.programID);
    } finally {
      super.finalize();
    }
  }
}

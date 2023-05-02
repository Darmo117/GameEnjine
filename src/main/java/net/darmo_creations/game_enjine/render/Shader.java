package net.darmo_creations.game_enjine.render;

import org.joml.Matrix4f;
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

  public Shader(String name) throws IOException {
    this.programID = glCreateProgram();

    this.vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(this.vertexShaderID, this.readFile(name + ".vert"));
    glCompileShader(this.vertexShaderID);
    if (glGetShaderi(this.vertexShaderID, GL_COMPILE_STATUS) != 1) {
      throw new IOException(glGetShaderInfoLog(this.vertexShaderID));
    }

    this.fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(this.fragmentShaderID, this.readFile(name + ".frag"));
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

  public void setUniform(String name, int value) {
    int location = glGetUniformLocation(this.programID, name);
    if (location != -1) {
      glUniform1i(location, value);
    }
  }

  public void setUniform(String name, final Matrix4f value) {
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

  private String readFile(String fileName) throws IOException {
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

  public void delete() {
    glDetachShader(this.programID, this.vertexShaderID);
    glDetachShader(this.programID, this.fragmentShaderID);
    glDeleteShader(this.vertexShaderID);
    glDeleteShader(this.fragmentShaderID);
    glDeleteProgram(this.programID);
  }
}

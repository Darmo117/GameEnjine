#version 120

uniform sampler2D sampler_;
varying vec2 tex_coords;

void main() {
  gl_FragColor = texture2D(sampler_, tex_coords);
}

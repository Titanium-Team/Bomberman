#version 110


attribute vec2 position;
attribute float tid;
attribute vec4 color;
attribute vec2 texCoords;

uniform mat4 combinedMatrix;

varying vec2 v_texCoords;
varying vec2 v_position;

void main() {
	v_texCoords = texCoords;
	v_position = position;

	gl_Position = vec4(position.xy, 0.0, 1.0) * combinedMatrix;
}
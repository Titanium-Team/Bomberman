#version 110

attribute vec2 position;
attribute float tid;
attribute vec4 color;
attribute vec2 texCoords;

uniform mat4 projectionMatrix;

varying vec4 v_color;
varying float v_tid;
varying vec2 v_texCoords;

void main() {
	v_color = color;
	v_tid = tid;
	v_texCoords = texCoords;
	gl_Position = vec4(position.xy, 0.0, 1.0) * projectionMatrix;
}
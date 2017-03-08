#version 110

#define PI 3.14

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D texture0;//shadow map

float sampleShadowMap(vec2 coord, float r) {
    return step(r, texture2D(texture0, coord).r);
}

void main() {
	vec4 color = v_color;

	vec2 coords = v_texCoords.st * 2.0 - 1.0;
	float r = length(coords);
	float intensity = smoothstep(1.0, 0.0, r);

	float theta = -atan(coords.y, coords.x);
	vec2 tc = vec2((theta + PI) / (2.0 * PI), 0.0);

	float shadowFactor = sampleShadowMap(tc, r);
	
	gl_FragColor = vec4(color.rgb, intensity * shadowFactor);
}
#version 110

#define PI 3.14

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D texture0;//shadow map
uniform float u_resolution;

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

	float center = sampleShadowMap(tc, r);

    float blur = (1. / u_resolution) * smoothstep(0., 1., r);

    float sum = 0.0;

    sum += sampleShadowMap(vec2(tc.x - 4.0*blur, tc.y), r) * 0.05;
    sum += sampleShadowMap(vec2(tc.x - 3.0*blur, tc.y), r) * 0.09;
    sum += sampleShadowMap(vec2(tc.x - 2.0*blur, tc.y), r) * 0.12;
    sum += sampleShadowMap(vec2(tc.x - 1.0*blur, tc.y), r) * 0.15;

    sum += center * 0.16;

    sum += sampleShadowMap(vec2(tc.x + 1.0*blur, tc.y), r) * 0.15;
    sum += sampleShadowMap(vec2(tc.x + 2.0*blur, tc.y), r) * 0.12;
    sum += sampleShadowMap(vec2(tc.x + 3.0*blur, tc.y), r) * 0.09;
    sum += sampleShadowMap(vec2(tc.x + 4.0*blur, tc.y), r) * 0.05;
	
	gl_FragColor = vec4(color.rgb, intensity * sum);
}
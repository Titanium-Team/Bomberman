#version 110

#define PI 3.14

varying vec2 v_texCoords;
varying vec2 v_position;

uniform sampler2D texture0;//occluders map
uniform float u_resolution;

const float THRESHOLD = 0.75;

void main() {
	float distance = 1.0;

    for (float y = 0.0; y < u_resolution; y += 1.0) {
        vec2 norm = vec2(v_texCoords.s, y / u_resolution) * 2.0 - 1.0;
        float theta = PI * 1.5 + norm.x * PI;
        float r = (1.0 + norm.y) * 0.5;

        vec2 coord = vec2(-r * sin(theta), -r * cos(theta)) / 2.0 + 0.5;

        vec4 data = texture2D(texture0, coord);

        float dst = y / u_resolution;

        float caster = data.a;
        if (caster > THRESHOLD) {
            distance = min(distance, dst);
        }
    }

    gl_FragColor = vec4(distance, 0.0, 0.0, 1.0);
}
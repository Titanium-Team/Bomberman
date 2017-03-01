#version 110

varying vec4 v_color;
varying float v_tid;
varying vec2 v_texCoords;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D texture4;
uniform sampler2D texture5;
uniform sampler2D texture6;
uniform sampler2D texture7;
uniform sampler2D texture8;
uniform sampler2D texture9;
uniform sampler2D texture10;
uniform sampler2D texture11;
uniform sampler2D texture12;
uniform sampler2D texture13;
uniform sampler2D texture14;
uniform sampler2D texture15;

void main() {
	vec4 color = v_color;
	
	int tid = int(v_tid);
	if (tid == 0)
		color *= texture2D(texture0, v_texCoords);
	else if (tid == 1)
		color *= texture2D(texture1, v_texCoords);
	else if (tid == 2)
		color *= texture2D(texture2, v_texCoords);
	else if (tid == 3)
		color *= texture2D(texture3, v_texCoords);
	else if (tid == 4)
		color *= texture2D(texture4, v_texCoords);
	else if (tid == 5)
		color *= texture2D(texture5, v_texCoords);
	else if (tid == 6)
		color *= texture2D(texture6, v_texCoords);
	else if (tid == 7)
		color *= texture2D(texture7, v_texCoords);
	else if (tid == 8)
		color *= texture2D(texture8, v_texCoords);
	else if (tid == 9)
		color *= texture2D(texture9, v_texCoords);
	else if (tid == 10)
		color *= texture2D(texture10, v_texCoords);
	else if (tid == 11)
		color *= texture2D(texture11, v_texCoords);
	else if (tid == 12)
		color *= texture2D(texture12, v_texCoords);
	else if (tid == 13)
		color *= texture2D(texture13, v_texCoords);
	else if (tid == 14)
		color *= texture2D(texture14, v_texCoords);
	else if (tid == 15)
		color *= texture2D(texture15, v_texCoords);
	
	gl_FragColor = color;
}
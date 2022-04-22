#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;

void main() {

    mat4 tm = transformationMatrix;
    tm[3][0] = (tm[3][0] + 0.5f) * 2.0f;
    tm[3][1] = (tm[3][1] - 0.5f) * 2.0f;

    gl_Position = tm * vec4(position, 0.0, 1.0);
    pass_textureCoords = textureCoords;
}

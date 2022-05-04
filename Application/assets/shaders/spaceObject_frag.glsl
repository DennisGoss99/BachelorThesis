#version 330 core

out vec4 color;

uniform bool collision;

void main() {
    if(collision)
        color = vec4(1, 0, 0, 1);
    else
        color = vec4(1, 1, 1, 1);
}
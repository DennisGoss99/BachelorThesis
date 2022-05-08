#version 330 core

in flat int collision2;

out vec4 color;

uniform bool collision;

void main() {
    if(collision2 == 1)
        color = vec4(1, 0, 0, 1);
    else
        color = vec4(1, 1, 1, 1);
}
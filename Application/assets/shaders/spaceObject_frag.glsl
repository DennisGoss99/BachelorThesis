#version 330 core

flat in uint collision;

out vec4 color;

void main() {
    if(collision == 1)
        color = vec4(1, 0, 0, 1);
    else
        color = vec4(1, 1, 1, 1);
}
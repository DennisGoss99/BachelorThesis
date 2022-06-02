#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in mat4 model_matrix;
layout(location = 5) in float collision_in;

//uniform mat4 model_matrix;
//uniform float collision_in;

out float collision;

uniform mat4 view_matrix;
uniform mat4 projection_matrix;

void main(){

    mat4 modelView = view_matrix * model_matrix;
    gl_Position =  projection_matrix * modelView * vec4(position, 1.0f);

    collision = (collision_in == 1.0f) ? 1 : 0;
}
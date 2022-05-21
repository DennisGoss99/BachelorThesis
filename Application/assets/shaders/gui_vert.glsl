#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 fontTextureCoords;

out vec2 textureCoords;

uniform mat4 transformationMatrix;

// 0 GUI-Element ; 1 Text
uniform int elementType;


void main(void){

    if(elementType == 0){
        //GUI
        gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
        textureCoords = vec2((position.x + 1) / 2.0, (position.y + 1) / 2.0);

    }else{
        //Text

        mat4 tm = transformationMatrix;
        tm[3][0] = (tm[3][0] + 1.0f);
        tm[3][1] = (tm[3][1] - 1.0f);

        gl_Position = tm * vec4(position, 0.0, 1.0);
        textureCoords = fontTextureCoords;
    }
}

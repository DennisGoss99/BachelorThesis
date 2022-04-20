#version 330 core

in vec2 textureCoords;

out vec4 color;

uniform sampler2D texture2D;
uniform int useImage;
uniform vec4 elementColor;

void main(void){

    if(useImage == 1 ){
        vec4 imgColor = texture(texture2D, textureCoords);
        color = (imgColor * imgColor.a) + (elementColor * (1 - imgColor.a));
    }
    else
        color = elementColor;
}

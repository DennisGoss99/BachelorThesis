#version 330 core

in vec2 textureCoords;
in vec4 gl_FragCoord;

out vec4 color;

uniform sampler2D texture2D;
uniform int useImage;
uniform vec4 elementColor;
uniform vec4 elementCorners;
uniform int borderRadius;

bool renderCornerPixel(){
    if(gl_FragCoord.x < elementCorners.x + borderRadius)
    {
        //upper left
        if(gl_FragCoord.y > elementCorners.y - borderRadius){
            return length(vec2(elementCorners.x + borderRadius - gl_FragCoord.x, elementCorners.y - borderRadius - gl_FragCoord.y)) < borderRadius;
        //lower left
        }else if(gl_FragCoord.y < elementCorners.w + borderRadius){
            return length(vec2(elementCorners.x + borderRadius - gl_FragCoord.x, elementCorners.w + borderRadius - gl_FragCoord.y)) < borderRadius;
        }
    }else if(gl_FragCoord.x > elementCorners.z - borderRadius){
        //upper right
        if(gl_FragCoord.y > elementCorners.y - borderRadius){
            return length(vec2(elementCorners.z - borderRadius - gl_FragCoord.x, elementCorners.y - borderRadius - gl_FragCoord.y)) < borderRadius;
            //lower right
        }else if(gl_FragCoord.y < elementCorners.w + borderRadius){
            return length(vec2(elementCorners.z - borderRadius - gl_FragCoord.x, elementCorners.w + borderRadius - gl_FragCoord.y)) < borderRadius;
        }
    }
    return true;
}

void main(void){

    //BorderAlgorithm
    if(borderRadius == 0 || renderCornerPixel())
        if(useImage == 1 ){
            vec4 imgColor = texture(texture2D, textureCoords);
            color = (imgColor * imgColor.a) + (elementColor * (1 - imgColor.a));
        }
        else
            color = elementColor;
}

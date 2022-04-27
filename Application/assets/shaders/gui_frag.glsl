#version 330 core

in vec2 textureCoords;
in vec4 gl_FragCoord;

out vec4 color;

uniform sampler2D texture2D;
uniform int useImage;
uniform vec4 elementColor;
uniform vec4 elementCorners;
uniform int borderRadius;

const float smoothness = 0.7;

float renderCornerPixel(){
    if(gl_FragCoord.x < elementCorners.x + borderRadius)
    {
        //upper left
        if(gl_FragCoord.y > elementCorners.y - borderRadius){
            return 1.0f - smoothstep(borderRadius - smoothness, borderRadius + smoothness, length(vec2(elementCorners.x + borderRadius - gl_FragCoord.x, elementCorners.y - borderRadius - gl_FragCoord.y)));
        //lower left
        }else if(gl_FragCoord.y < elementCorners.w + borderRadius){
            return 1.0f - smoothstep(borderRadius - smoothness, borderRadius + smoothness, length(vec2(elementCorners.x + borderRadius - gl_FragCoord.x, elementCorners.w + borderRadius - gl_FragCoord.y)));
        }
    }else if(gl_FragCoord.x > elementCorners.z - borderRadius){
        //upper right
        if(gl_FragCoord.y > elementCorners.y - borderRadius){
            return 1.0f - smoothstep(borderRadius - smoothness, borderRadius + smoothness, length(vec2(elementCorners.z - borderRadius - gl_FragCoord.x, elementCorners.y - borderRadius - gl_FragCoord.y)));
        //lower right
        }else if(gl_FragCoord.y < elementCorners.w + borderRadius){
            return 1.0f - smoothstep(borderRadius - smoothness, borderRadius + smoothness, length(vec2(elementCorners.z - borderRadius - gl_FragCoord.x, elementCorners.w + borderRadius - gl_FragCoord.y)));
        }
    }
    return 1.0f;
}

void main(void){

    float alphaValue = 1.0f;

    //BorderAlgorithm
    if(borderRadius > 0)
        alphaValue = renderCornerPixel();

    if(useImage == 1 ){
        vec4 imgColor = texture(texture2D, textureCoords);
        imgColor.a *= alphaValue;
        color = (imgColor * imgColor.a) + (elementColor * (1 - imgColor.a));
    }
    else {
        color = vec4(elementColor.rgb, elementColor.a * alphaValue);
    }
}

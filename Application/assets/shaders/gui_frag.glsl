#version 330 core

in vec2 textureCoords;
in vec4 gl_FragCoord;

out vec4 color;

uniform sampler2D texture2D;
uniform bool useImage;
uniform vec4 elementColor;

uniform vec4 elementCorners;
uniform int cornerRadius;

// used for scrollbars
uniform bool limitRenderArea;


const float smoothness = 0.7;

float renderCornerPixel(){
    if(gl_FragCoord.x < elementCorners.x + cornerRadius)
    {
        //upper left
        if(gl_FragCoord.y > elementCorners.y - cornerRadius){
            return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(elementCorners.x + cornerRadius - gl_FragCoord.x, elementCorners.y - cornerRadius - gl_FragCoord.y)));
        //lower left
        }else if(gl_FragCoord.y < elementCorners.w + cornerRadius){
            return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(elementCorners.x + cornerRadius - gl_FragCoord.x, elementCorners.w + cornerRadius - gl_FragCoord.y)));
        }
    }else if(gl_FragCoord.x > elementCorners.z - cornerRadius){
        //upper right
        if(gl_FragCoord.y > elementCorners.y - cornerRadius){
            return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(elementCorners.z - cornerRadius - gl_FragCoord.x, elementCorners.y - cornerRadius - gl_FragCoord.y)));
        //lower right
        }else if(gl_FragCoord.y < elementCorners.w + cornerRadius){
            return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(elementCorners.z - cornerRadius - gl_FragCoord.x, elementCorners.w + cornerRadius - gl_FragCoord.y)));
        }
    }
    return 1.0;
}

void main(void){

    float alphaValue = 1.0;

    //BorderAlgorithm
    if(cornerRadius > 0)
        alphaValue = renderCornerPixel();

    // Outside of scrollBox
    if(limitRenderArea)
        if(elementCorners.x > gl_FragCoord.x || gl_FragCoord.x > elementCorners.z  || elementCorners.y < gl_FragCoord.y || gl_FragCoord.y < elementCorners.w)
            return;

    if(useImage){
        vec4 imgColor = texture(texture2D, textureCoords);
        imgColor.a *= alphaValue;
        color = (imgColor * imgColor.a) + (elementColor * (1 - imgColor.a));
    }
    else
        color = vec4(elementColor.rgb, elementColor.a * alphaValue);
}

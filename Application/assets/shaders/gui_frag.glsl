#version 330 core

in vec2 textureCoords;
in vec4 gl_FragCoord;

out vec4 color;

uniform sampler2D texture2D;
uniform vec4 elementColor;

// 0 GUI-Element ; 1 Text
uniform int elementType;

// Gui Element Variables

    uniform bool useImage;

    uniform int cornerRadius;

    // used for scrollbars
    uniform bool limitRenderArea;
    uniform vec4 elementCorners;

    // used for rounded Elements
    const float smoothness = 0.7;
    uniform vec4 roundedElementCorners;
    float renderCornerPixel(){
        if(gl_FragCoord.x < roundedElementCorners.x + cornerRadius)
        {
            //upper left
            if(gl_FragCoord.y > roundedElementCorners.y - cornerRadius){
                return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(roundedElementCorners.x + cornerRadius - gl_FragCoord.x, roundedElementCorners.y - cornerRadius - gl_FragCoord.y)));
            //lower left
            }else if(gl_FragCoord.y < roundedElementCorners.w + cornerRadius){
                return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(roundedElementCorners.x + cornerRadius - gl_FragCoord.x, roundedElementCorners.w + cornerRadius - gl_FragCoord.y)));
            }
        }else if(gl_FragCoord.x > roundedElementCorners.z - cornerRadius){
            //upper right
            if(gl_FragCoord.y > roundedElementCorners.y - cornerRadius){
                return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(roundedElementCorners.z - cornerRadius - gl_FragCoord.x, roundedElementCorners.y - cornerRadius - gl_FragCoord.y)));
            //lower right
            }else if(gl_FragCoord.y < roundedElementCorners.w + cornerRadius){
                return 1.0 - smoothstep(cornerRadius - smoothness, cornerRadius + smoothness, length(vec2(roundedElementCorners.z - cornerRadius - gl_FragCoord.x, roundedElementCorners.w + cornerRadius - gl_FragCoord.y)));
            }
        }
        return 1.0;
    }

// Font Variables

    const float width = 0.5;
    const float edge = 0.1;

void main(void){

    // Outside of scrollBox
    if(limitRenderArea)
        if(elementCorners.x > gl_FragCoord.x || gl_FragCoord.x > elementCorners.z  || elementCorners.y < gl_FragCoord.y || gl_FragCoord.y < elementCorners.w)
            return;


    if(elementType == 0){
        //GUI

        float alphaValue = 1.0;

        //BorderAlgorithm
        if(cornerRadius > 0)
        alphaValue = renderCornerPixel();



        if(useImage){
            vec4 imgColor = texture(texture2D, textureCoords);
            imgColor.a *= alphaValue;
            color = (imgColor * imgColor.a) + (elementColor * (1 - imgColor.a));
        }
        else
        color = vec4(elementColor.rgb, elementColor.a * alphaValue);

    }else{
        //Text

        float distance = 1 - texture(texture2D, textureCoords).a;
        float alpha = 1 - smoothstep(width, width + edge, distance);

        color = vec4(elementColor.xyz, alpha * elementColor.a);
    }
}

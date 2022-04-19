#version 330 core

#define MAX_POINTLIGHTS 4
#define MAX_SPOTLIGHTS 4

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 texcoord;
    float originAngle;
    vec3 normal;
} vertexData;

//Uniforms
uniform sampler2D diff;
uniform sampler2D emit;
uniform sampler2D spec;

uniform sampler2D overlay;

uniform vec3 emitColor;
uniform float shininess;

uniform float time;
uniform int useOverlay;

//fragment shader output
out vec4 color;

void main(){

    vec4 diffTexture = texture(diff, vertexData.texcoord);
    vec4 emitTexture = texture(emit, vertexData.texcoord);
    vec4 specTexture = texture(spec, vertexData.texcoord);

    if(useOverlay == 1){

        vec4 overlayTexture = texture(overlay, vec2(vertexData.texcoord.x - time/ 75.0f, vertexData.texcoord.y));
        float bendOverlayAmount = 1 - (overlayTexture.r + overlayTexture.g + overlayTexture.b) / 3;

        //Set overlayTexture to its % value
        overlayTexture = overlayTexture * (1 - bendOverlayAmount);

        diffTexture = texture(diff, vertexData.texcoord) * bendOverlayAmount + overlayTexture;

        emitTexture = texture(emit, vertexData.texcoord) * bendOverlayAmount;
        specTexture = texture(spec, vertexData.texcoord) * bendOverlayAmount;

    }

    // set emmitColor
    //emitTexture = emitTexture * vec4(emitColor,0.0f);


    // normalize everything necessary //
    vec3 n = normalize(vertexData.normal);
    vec3 viewDirection = vertexData.position.xyz;

    vec4 ambiantLight = diffTexture * min(1.0f, max(0.2f,vertexData.originAngle));

    color = emitTexture + ambiantLight;// + diffusePointsSum + diffuseSpotSum;

}


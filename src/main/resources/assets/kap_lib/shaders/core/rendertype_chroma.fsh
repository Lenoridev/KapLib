#version 150

#moj_import <fog.glsl>
#moj_import <kap_lib:extras.glsl>

const float DEFAULT_COLOR_WIDTH = 10.0;
const float GAME_TIME_SCALE = 200.0;

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 config;

out vec4 fragColor;

//TODO add config

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

    int locConfig = int(config.a * 255);

    bool type1 = getBit(locConfig, 3);
    bool type2 = getBit(locConfig, 4);
    float l;
    vec2 stage = normalizeCoord(gl_FragCoord.xy / ScreenSize.xy, getBit(locConfig, 1), getBit(locConfig, 2));
    if (!(type1 || type2)) {
        l = length(stage);
    } else if (type1 && !type2) {
        l = max(stage.x, stage.y);
    } else {
        l = (stage.x + stage.y);
    }
    l *= DEFAULT_COLOR_WIDTH;
    color = vec4(hsb2rgb(vec3(fract(l + GameTime * GAME_TIME_SCALE), 1.0, 1.0)), color.a);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}

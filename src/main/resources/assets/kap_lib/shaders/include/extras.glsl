vec3 hsb2rgb( in vec3 c ){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),
    6.0)-3.0)-1.0, 0.0, 1.0 );
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix( vec3(1.0), rgb, c.y);
}

vec2 normalizeCoord(in vec2 stage, bool x, bool y) {
    return vec2(x ? 1. - stage.x : stage.x, y ? 1. - stage.y : stage.y);
}

bool getBit(int i, int bit) {
    return mod(i, pow(2, bit + 1)) >= pow(2, bit);
}
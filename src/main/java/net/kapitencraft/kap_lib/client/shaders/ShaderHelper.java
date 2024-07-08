package net.kapitencraft.kap_lib.client.shaders;

import net.minecraft.client.renderer.ShaderInstance;

public class ShaderHelper {


    public static void updateUniforms(ShaderInstance instance) {
        UniformsProvider.applyVectors(instance);
        UniformsProvider.applySingletons(instance);
    }
}

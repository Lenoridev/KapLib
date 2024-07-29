package net.kapitencraft.kap_lib.client.shaders;

import net.minecraft.client.renderer.RenderStateShard;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ModShaderStateShards {
    public static final RenderStateShard.ShaderStateShard CHROMATIC = new RenderStateShard.ShaderStateShard(ModShaders::getRendertypeChromaShader);
}
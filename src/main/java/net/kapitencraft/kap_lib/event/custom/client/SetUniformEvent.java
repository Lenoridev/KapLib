package net.kapitencraft.kap_lib.event.custom.client;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class SetUniformEvent extends Event {


    public SetUniformEvent(ShaderInstance instance) {
    }
}

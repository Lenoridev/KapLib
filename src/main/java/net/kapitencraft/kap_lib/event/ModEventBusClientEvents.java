package net.kapitencraft.kap_lib.event;

import net.kapitencraft.kap_lib.client.particle.DamageIndicatorParticle;
import net.kapitencraft.kap_lib.client.particle.ShimmerShieldParticle;
import net.kapitencraft.kap_lib.registry.ModParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpecial(ModParticleTypes.DAMAGE_INDICATOR.get(), new DamageIndicatorParticle.Provider());
        event.registerSprite(ModParticleTypes.SHIMMER_SHIELD.get(), new ShimmerShieldParticle.Provider());
    }
}

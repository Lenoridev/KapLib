package net.kapitencraft.kap_lib.registry;

import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.client.particle.DamageIndicatorParticleOptions;
import net.kapitencraft.kap_lib.client.particle.animation.ParticleAnimationInfo;
import net.kapitencraft.kap_lib.client.particle.animation.ParticleAnimationOptions;
import net.kapitencraft.kap_lib.client.particle.animation.ParticleAnimationParameters;
import net.kapitencraft.kap_lib.helpers.TextHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModParticleTypes {
    DeferredRegister<ParticleType<?>> REGISTRY = KapLibMod.registry(ForgeRegistries.PARTICLE_TYPES);

    RegistryObject<ParticleAnimationOptions> ANIMATION = REGISTRY.register("animation", () -> new ParticleAnimationOptions(ParticleTypes.ASH, ParticleAnimationParameters.create(), ParticleAnimationInfo.EMPTY, 1));
    RegistryObject<DamageIndicatorParticleOptions> DAMAGE_INDICATOR = REGISTRY.register("damage_indicator", () -> new DamageIndicatorParticleOptions(TextHelper.damageIndicatorCoder("heal"), 1, 1));

}

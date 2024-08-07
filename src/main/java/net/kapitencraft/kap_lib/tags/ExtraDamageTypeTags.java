package net.kapitencraft.kap_lib.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public interface ExtraDamageTypeTags {
    TagKey<DamageType> MAGIC = forgeKey("magic");
    TagKey<DamageType> PARTICLE_WEAPON = forgeKey("particle_weapon");

    private static TagKey<DamageType> forgeKey(String subName) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("forge", subName));
    }
}

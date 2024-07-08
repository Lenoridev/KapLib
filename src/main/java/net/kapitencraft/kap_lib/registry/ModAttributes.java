package net.kapitencraft.kap_lib.registry;

import net.kapitencraft.kap_lib.KapLibMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModAttributes {
    DeferredRegister<Attribute> REGISTRY = KapLibMod.registry(ForgeRegistries.ATTRIBUTES);

    private static RegistryObject<Attribute> register(String name, double initValue, double minValue, double maxValue) {
        return REGISTRY.register("generic." + name, ()-> new RangedAttribute("generic." + name, initValue, minValue, maxValue).setSyncable(true));
    }

    RegistryObject<Attribute> COOLDOWN_REDUCTION = register("cooldown_reduction", 0, 0, 100);
}

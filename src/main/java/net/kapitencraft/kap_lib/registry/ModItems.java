package net.kapitencraft.kap_lib.registry;

import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.item.combat.armor.CrimsonArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModItems {
    DeferredRegister<Item> REGISTRY = KapLibMod.registry(ForgeRegistries.ITEMS);

    RegistryObject<ArmorItem> HELMET = REGISTRY.register("helmet", ()-> new CrimsonArmorItem(ArmorItem.Type.HELMET));
    RegistryObject<ArmorItem> CHEST = REGISTRY.register("chest", ()-> new CrimsonArmorItem(ArmorItem.Type.CHESTPLATE));
    RegistryObject<ArmorItem> LEGS = REGISTRY.register("legs", ()-> new CrimsonArmorItem(ArmorItem.Type.LEGGINGS));
    RegistryObject<ArmorItem> BOOTS = REGISTRY.register("boots", ()-> new CrimsonArmorItem(ArmorItem.Type.BOOTS));
}

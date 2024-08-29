package net.kapitencraft.kap_lib.requirements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RequirementType<T> {
    public static final RequirementType<Item> ITEM = new RequirementType<>("item", ForgeRegistries.ITEMS);
    public static final RequirementType<Enchantment> ENCHANTMENT = new RequirementType<>("enchantment", ForgeRegistries.ENCHANTMENTS);

    private final String name;
    private final IForgeRegistry<T> registry;

    public RequirementType(String name, IForgeRegistry<T> registry) {
        this.name = name;
        this.registry = registry;
    }

    public ResourceLocation getId(T value) {
        return this.registry.getKey(value);
    }

    public T getById(ResourceLocation location) {
        return this.registry.getValue(location);
    }

    public String getName() {
        return name;
    }
}
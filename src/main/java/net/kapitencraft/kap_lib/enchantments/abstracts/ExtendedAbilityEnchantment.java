package net.kapitencraft.kap_lib.enchantments.abstracts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class ExtendedAbilityEnchantment extends Enchantment implements ModEnchantment {
    protected ExtendedAbilityEnchantment(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot[] p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }

    public abstract void onTick(LivingEntity source, int level);
}

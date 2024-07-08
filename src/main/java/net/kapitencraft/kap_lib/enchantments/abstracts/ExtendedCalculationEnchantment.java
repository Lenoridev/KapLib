package net.kapitencraft.kap_lib.enchantments.abstracts;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.HashMap;
import java.util.Map;

public abstract class ExtendedCalculationEnchantment extends Enchantment implements ModEnchantment {

    private final CalculationType type;
    private final ProcessPriority priority;

    public static Map<ExtendedCalculationEnchantment, Integer> getAllEnchantments(ItemStack stack, boolean isWeapon) {
        Map<ExtendedCalculationEnchantment, Integer> map = new HashMap<>();
        for (Enchantment enchantment : stack.getAllEnchantments().keySet()) {
            if (enchantment instanceof ExtendedCalculationEnchantment extended) {
                if ((isWeapon && enchantment instanceof IWeaponEnchantment) || (!isWeapon && enchantment instanceof IArmorEnchantment)) {
                    map.put(extended, stack.getAllEnchantments().get(extended));
                }
            }
        }
        return map;
    }

    public static float runWithPriority(ItemStack enchanted, LivingEntity attacker, LivingEntity attacked, double damage, MiscHelper.DamageType type, boolean isWeapon, DamageSource source) {
        Map<ExtendedCalculationEnchantment, Integer> enchantmentIntegerMap = getAllEnchantments(enchanted, isWeapon);
        for (ProcessPriority priority : ProcessPriority.values()) {
            for (ExtendedCalculationEnchantment enchantment : enchantmentIntegerMap.keySet()) {
                if (enchantment.priority == priority) {
                    damage = enchantment.tryExecute(enchantmentIntegerMap.get(enchantment), enchanted, attacker, attacked, damage, type, source);
                }
            }
        }
        return (float) damage;
    }

    protected ExtendedCalculationEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots, CalculationType type, ProcessPriority priority) {
        super(rarity, category, slots);
        this.type = type;
        this.priority = priority;
    }

    public ProcessPriority getPriority() {
        return priority;
    }

    public double tryExecute(int level, ItemStack enchanted, LivingEntity attacker, LivingEntity attacked, double damage, MiscHelper.DamageType type, DamageSource source) {
        if (this.type.contains(type)) {
            return this.execute(level, enchanted, attacker, attacked, damage, source);
        }
        return damage;
    }

    protected abstract double execute(int level, ItemStack enchanted, LivingEntity attacker, LivingEntity attacked, double damage, DamageSource source);

    public enum CalculationType {
        ONLY_MAGIC(MiscHelper.DamageType.MAGIC),
        ONLY_MELEE(MiscHelper.DamageType.MELEE),
        ONLY_RANGED(MiscHelper.DamageType.RANGED),
        ALL(MiscHelper.DamageType.MAGIC, MiscHelper.DamageType.RANGED, MiscHelper.DamageType.MELEE),
        ALL_RANGED(MiscHelper.DamageType.RANGED, MiscHelper.DamageType.MAGIC);

        private final MiscHelper.DamageType[] types;

        CalculationType(MiscHelper.DamageType... types) {
            this.types = types;
        }

        public boolean contains(MiscHelper.DamageType type) {
            for (MiscHelper.DamageType type1 : this.types) {
                if (type1 == type) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum ProcessPriority {
        HIGHEST,
        HIGH,
        MEDIUM,
        LOW,
        LOWEST;
    }
}

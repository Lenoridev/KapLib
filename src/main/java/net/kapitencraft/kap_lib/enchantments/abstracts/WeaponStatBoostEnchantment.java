package net.kapitencraft.kap_lib.enchantments.abstracts;

public abstract class WeaponStatBoostEnchantment extends StatBoostEnchantment implements IWeaponEnchantment {
    protected WeaponStatBoostEnchantment(Rarity p_44676_) {
        super(p_44676_, ModEnchantmentCategories.ALL_WEAPONS, DEFAULT_SLOT);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}

package net.kapitencraft.kap_lib.enchantments.abstracts;

public interface ModEnchantment {

    default Object[] getDescriptionMods(int level) {
        return new Object[]  {level};
    }
}

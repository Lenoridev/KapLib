package net.kapitencraft.kap_lib.item.combat.armor;

import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.item.combat.armor.client.model.CrimsonArmorModel;
import net.kapitencraft.kap_lib.item.combat.armor.client.renderer.ArmorRenderer;
import net.kapitencraft.kap_lib.util.ExtraRarities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CrimsonArmorItem extends ModArmorItem {
    public CrimsonArmorItem(Type pType) {
        super(ArmorMaterials.NETHERITE, pType, new Properties().rarity(ExtraRarities.DIVINE));
    }

    @Override
    protected boolean withCustomModel() {
        return true;
    }

    @Override
    protected ArmorRenderer<?> getRenderer(LivingEntity living, ItemStack stack, EquipmentSlot slot) {
        return new ArmorRenderer<>(CrimsonArmorModel::createBodyLayer, CrimsonArmorModel::new);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return makeCustomTextureLocation(KapLibMod.MOD_ID, "crimson_armor");
    }
}

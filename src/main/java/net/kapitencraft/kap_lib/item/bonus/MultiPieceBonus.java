package net.kapitencraft.kap_lib.item.bonus;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public abstract class MultiPieceBonus implements Bonus {
    private final String name;

    public MultiPieceBonus(String name) {
        this.name = name;
    }


    @Override
    public void onEntityKilled(LivingEntity killed, LivingEntity user, MiscHelper.DamageType type) {

    }

    @Override
    public void onTick(Level level, @NotNull LivingEntity entity) {

    }

    public abstract EnumMap<EquipmentSlot, @Nullable RegistryObject<? extends Item>> getReqPieces();

    public boolean isActive(LivingEntity living, EquipmentSlot self) {
        boolean flag = true;
        for (EquipmentSlot slot : MiscHelper.ARMOR_EQUIPMENT) {
            RegistryObject<? extends Item> req = getReqPieces().get(slot);
            if (slot == self || req == null) continue;
            Item slotItem = living.getItemBySlot(slot).getItem();
            flag &= slotItem == req.get() && slotItem instanceof IArmorBonusItem bonusItem && bonusItem.getPieceBonni().contains(this);
        }
        return flag;
    }


    @Override
    public String getName() {
        return name;
    }
}

package net.kapitencraft.kap_lib.entity.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NoFireItemEntity extends ItemEntity {
    public NoFireItemEntity(Level p_32001_, double x, double y, double z, ItemStack p_32005_) {
        super(p_32001_, x, y, z, p_32005_);
    }

    public static NoFireItemEntity copy(ItemEntity main) {
        NoFireItemEntity noFireItemEntity = new NoFireItemEntity(main.level(), main.getX(), main.getY(), main.getZ(), main.getItem());
        noFireItemEntity.setDeltaMovement(main.getDeltaMovement());
        return noFireItemEntity;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
}

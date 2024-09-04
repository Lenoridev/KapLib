package net.kapitencraft.kap_lib.mixin.classes;

import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.kapitencraft.kap_lib.requirements.RequirementType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IForgeLivingEntity {
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot pSlot);

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "updateFallFlying", at = @At(value = "HEAD"), cancellable = true)
    private void checkRequirements(CallbackInfo ci) {
        if (!level().isClientSide() && !RequirementManager.instance.meetsRequirements(RequirementType.ITEM, getItemBySlot(EquipmentSlot.CHEST).getItem(), self())) {
            setSharedFlag(7, false);
            ci.cancel();
        }
    }
}

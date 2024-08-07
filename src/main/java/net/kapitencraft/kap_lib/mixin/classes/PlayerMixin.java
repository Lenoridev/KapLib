package net.kapitencraft.kap_lib.mixin.classes;

import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.kapitencraft.kap_lib.requirements.RequirementType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique
    private Player kapLib$self() {
        return (Player) (Object) this;
    }

    //TODO f*** fix
    @Inject(method = "startFallFlying", at = @At("HEAD"), cancellable = true)
    private void checkCanFallFly(CallbackInfo ci) {
        if (RequirementManager.instance.meetsRequirements(RequirementType.ITEM, kapLib$self().getItemBySlot(EquipmentSlot.CHEST).getItem(), kapLib$self())) ci.cancel();
    }
}

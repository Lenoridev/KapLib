package net.kapitencraft.kap_lib.mixin.classes;

import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.kapitencraft.kap_lib.requirements.RequirementType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class PlayerMixin {

    private Player self() {
        return (Player) (Object) this;
    }

    @Inject(method = "tryToStartFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void checkReqs(CallbackInfoReturnable<Boolean> cir, ItemStack itemstack) {
        if (!RequirementManager.instance.meetsRequirements(RequirementType.ITEM, itemstack.getItem(), self())) {
            cir.cancel();
        }
    }
}

package net.kapitencraft.kap_lib.mixin.classes;

import net.kapitencraft.kap_lib.event.custom.LivingStartGlidingEvent;
import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.kapitencraft.kap_lib.requirements.RequirementType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgePlayer;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public class PlayerMixin implements IForgePlayer {

    private Player self() {
        return (Player) (Object) this;
    }

    @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
    public void checkGlideAllowed(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = self().getItemBySlot(EquipmentSlot.CHEST);
        if (!RequirementManager.instance.meetsRequirements(RequirementType.ITEM, stack.getItem(), self())) {
            cir.setReturnValue(false);
        }
        LivingStartGlidingEvent event = new LivingStartGlidingEvent(self(), stack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) cir.setReturnValue(false);
    }
}

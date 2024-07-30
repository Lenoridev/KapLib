package net.kapitencraft.kap_lib.mixin.classes.client;

import net.kapitencraft.kap_lib.enchantments.extras.EnchantmentDescriptionManager;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void appendEnchantmentNames(List<Component> pTooltipComponents, ListTag pStoredEnchantments, Player player) {
        EnchantmentDescriptionManager.addAllTooltips(self(), pTooltipComponents, pStoredEnchantments, player);
    }
}
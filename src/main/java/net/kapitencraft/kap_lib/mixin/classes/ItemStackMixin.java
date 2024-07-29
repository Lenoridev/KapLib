package net.kapitencraft.kap_lib.mixin.classes;

import net.kapitencraft.kap_lib.enchantments.extras.EnchantmentDescriptionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void appendEnchantmentNames(List<Component> pTooltipComponents, ListTag pStoredEnchantments, Player player) {
        for(int i = 0; i < pStoredEnchantments.size(); ++i) {
            CompoundTag compoundtag = pStoredEnchantments.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundtag)).ifPresent((enchantment) -> {
            });
        }
    }

    /**
     * @author Kapitencraft
     * @reason add enchantment description
     */
    @Overwrite
    public static void appendEnchantmentNames(List<Component> pTooltipComponents, ListTag pStoredEnchantments) {
        for(int i = 0; i < pStoredEnchantments.size(); ++i) {
            CompoundTag compoundtag = pStoredEnchantments.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundtag)).ifPresent((p_41708_) -> {
                pTooltipComponents.add(p_41708_.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundtag)));
            });
        }

    }
}
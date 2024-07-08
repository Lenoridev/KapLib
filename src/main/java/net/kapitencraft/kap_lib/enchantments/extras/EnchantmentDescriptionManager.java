package net.kapitencraft.kap_lib.enchantments.extras;

import net.kapitencraft.kap_lib.enchantments.abstracts.ModEnchantment;
import net.kapitencraft.kap_lib.helpers.ClientHelper;
import net.kapitencraft.kap_lib.helpers.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@Mod.EventBusSubscriber()
public class EnchantmentDescriptionManager {

    public static void addTooltipForEnchant(ItemStack stack, List<Component> list, Enchantment enchantment, Player player) {
        if (fromBook(stack.getItem())) {
            if (!enchantment.isTradeable()) list.add(Component.translatable("ench_desc.not_tradeable").withStyle(ChatFormatting.YELLOW));
            if (enchantment.isTreasureOnly()) list.add(Component.translatable("ench_desc.treasure").withStyle(ChatFormatting.YELLOW));
        }
        ClientHelper.addReqContent(list::add, enchantment, player);
        list.addAll(getDescription(stack, enchantment));
    }

    public static boolean fromBook(Item item) {
        return item instanceof EnchantedBookItem;
    }

    public static List<Component> getDescription(ItemStack stack, Enchantment ench) {
        int level = stack.getItem() instanceof EnchantedBookItem ? (EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(stack)).get(ench)) : EnchantmentHelper.getTagEnchantmentLevel(ench, stack);
        Object[] objects = ench instanceof ModEnchantment modEnchantment ? modEnchantment.getDescriptionMods(level) : new String[]{String.valueOf(level)};
        Stream<String> stream = Arrays.stream(objects).map(String::valueOf);
        return TextHelper.getDescriptionList(ench.getDescriptionId(), component -> component.withStyle(ChatFormatting.DARK_GRAY), stream.map(TextHelper::wrapInRed).toArray());
    }
}
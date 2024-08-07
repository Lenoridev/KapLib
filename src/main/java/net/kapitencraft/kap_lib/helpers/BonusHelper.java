package net.kapitencraft.kap_lib.helpers;

import net.kapitencraft.kap_lib.collection.MapStream;
import net.kapitencraft.kap_lib.enchantments.abstracts.ExtendedAbilityEnchantment;
import net.kapitencraft.kap_lib.event.custom.ModEventFactory;
import net.kapitencraft.kap_lib.item.IEventListener;
import net.kapitencraft.kap_lib.item.bonus.IArmorBonusItem;
import net.kapitencraft.kap_lib.item.bonus.IItemBonusItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

@Mod.EventBusSubscriber
public interface BonusHelper {


    /**
     * @param living the target entity to tick enchantments of
     * ticks any {@link ExtendedAbilityEnchantment} applied to any of the 6 {@link EquipmentSlot}s available
     */
    static void tickEnchantments(LivingEntity living) {
        doForSlot((stack, slot) -> MapStream.of(stack.getAllEnchantments())
                .filterKeys(e -> e instanceof ExtendedAbilityEnchantment)
                .mapKeys(ExtendedAbilityEnchantment.class::cast)
                .forEach((enchantment, integer) -> enchantment.onTick(living, integer)), living, (stack, slot) -> stack.isEnchanted());
    }

    /**
     * @return all IEventListeners applied for the {@code stack} at the given {@code slot}
     */
    static List<IEventListener> getListenersFromStack(EquipmentSlot slot, ItemStack stack, LivingEntity living) {
        List<IEventListener> bonuses = new ArrayList<>();
        if (stack.getItem() instanceof IItemBonusItem bonusItem) {
            bonuses.add(bonusItem.getBonus());
            bonuses.add(bonusItem.getExtraBonus());
            if (stack.getItem() instanceof ArmorItem armorItem) bonuses.addAll(getArmorBonuses(armorItem, living, slot));
        }
        ModEventFactory.fetchItemBonuses(bonuses, stack, slot);
        return bonuses.stream().filter(Objects::nonNull).toList();
    }

    /**
     * @return the modifiers applied to the given {@code armorItem} at the given {@code slot}
     */
    private static List<IEventListener> getArmorBonuses(ArmorItem armorItem, LivingEntity living, EquipmentSlot slot) {
        List<IEventListener> list = new ArrayList<>();
        if (armorItem instanceof IArmorBonusItem armorBonusItem && armorItem.getEquipmentSlot() == slot) {
            armorBonusItem.getPieceBonni().stream().filter(multiPieceBonus -> multiPieceBonus.isActive(living, slot)).forEach(list::add);
            list.add(armorBonusItem.getExtraBonus(slot));
            list.add(armorBonusItem.getPieceBonusForSlot(slot));
        }
        return list;
    }


    /**
     * do something with each of the 6 {@link EquipmentSlot}s available on the {@link LivingEntity} given
     */
    static void doForSlot(BiConsumer<ItemStack, EquipmentSlot> stackConsumer, LivingEntity living, BiPredicate<ItemStack, EquipmentSlot> usagePredicate) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = living.getItemBySlot(slot);
            if (usagePredicate.test(stack, slot)) stackConsumer.accept(stack, slot);
        }
    }
}

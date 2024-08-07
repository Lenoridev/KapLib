package net.kapitencraft.kap_lib.event.custom;

import net.kapitencraft.kap_lib.item.IEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.List;

public class ModEventFactory {

    public static <T extends Event & IModBusEvent> void fireModEvent(T event) {
        ModLoader.get().postEvent(event);
    }

    public static void onLoadingItemStack(ItemStack stack) {
        ItemStackEvent.Load event = new ItemStackEvent.Load(stack);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onSavingItemStack(ItemStack stack, CompoundTag tag) {
        ItemStackEvent.Save event = new ItemStackEvent.Save(stack, tag);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void fetchItemBonuses(List<IEventListener> listeners, ItemStack stack, EquipmentSlot slot) {
        FetchItemBonusesEvent event = new FetchItemBonusesEvent(listeners, stack, slot);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
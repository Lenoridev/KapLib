package net.kapitencraft.kap_lib.event.custom;

import net.kapitencraft.kap_lib.item.IEventListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * used to add new {@link IEventListener}s to {@link ItemStack}s when equipped
 */
public class FetchItemBonusesEvent extends ItemStackEvent {
    private final List<IEventListener> listeners;
    private final EquipmentSlot slot;

    public FetchItemBonusesEvent(List<IEventListener> listeners, ItemStack stack, EquipmentSlot slot) {
        super(stack);
        this.listeners = listeners;
        this.slot = slot;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public void addListener(IEventListener listener) {
        listeners.add(listener);
    }
}

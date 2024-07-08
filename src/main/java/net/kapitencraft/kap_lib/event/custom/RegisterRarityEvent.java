package net.kapitencraft.kap_lib.event.custom;

import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.List;

public class RegisterRarityEvent extends Event implements IModBusEvent {

    private final List<Rarity> rarities;

    public RegisterRarityEvent(List<Rarity> rarities) {
        this.rarities = rarities;
    }

    public void addRarity(Rarity rarity) {
        this.rarities.add(rarity);
    }
}

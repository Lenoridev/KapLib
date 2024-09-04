package net.kapitencraft.kap_lib.event.custom;

import net.kapitencraft.kap_lib.util.IntegerModifierCollector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * called whenever a player starts using a FishingHook
 * <br> used to calculate changes on fishing hooks stats
 */
public class ModifyFishingHookStatsEvent extends Event {
    public final Entity hook;
    public final Player player;
    public final ItemStack fishingRod;

    public final IntegerModifierCollector lureSpeed;
    public final IntegerModifierCollector luck;
    public final IntegerModifierCollector hookSpeed;

    public ModifyFishingHookStatsEvent(Entity hook, Player player, int lureSpeed, int luck, ItemStack fishingRod) {
        this.hook = hook;
        this.player = player;
        this.fishingRod = fishingRod;
        this.hookSpeed = new IntegerModifierCollector();
        this.lureSpeed = new IntegerModifierCollector();
        this.luck = new IntegerModifierCollector();
        this.lureSpeed.setBase(lureSpeed);
        this.luck.setBase(luck);
    }
}

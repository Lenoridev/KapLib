package net.kapitencraft.kap_lib.event.custom;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * event to register custom anvil recipes to the handler <br>
 * use {@link net.kapitencraft.kap_lib.item.misc.AnvilUses#registerAnvilUse(BiPredicate, BiConsumer, int) AnvilUses#registerAnvilUse()} to register new ones
 */
public class RegisterAnvilUsesEvent extends Event implements IModBusEvent {
}

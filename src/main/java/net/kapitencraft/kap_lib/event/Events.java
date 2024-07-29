package net.kapitencraft.kap_lib.event;

import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class Events {
    private static final List<Class<? extends PlayerEvent>> dontCancel = List.of(
            EntityItemPickupEvent.class,
            ItemTooltipEvent.class,
            RenderPlayerEvent.Pre.class,
            RenderPlayerEvent.Post.class,
            PlayerEvent.LoadFromFile.class,
            PlayerEvent.NameFormat.class,
            PlayerEvent.TabListNameFormat.class,
            PlayerEvent.PlayerLoggedInEvent.class,
            PlayerEvent.PlayerLoggedOutEvent.class,
            MovementInputUpdateEvent.class
    );

    @SubscribeEvent
    public static void ensureReqsMet(PlayerEvent event) { //cancel any PlayerEvent that don't meet the item requirements
        if (!dontCancel.contains(event.getClass()) && !RequirementManager.meetsRequirementsFromEvent(event, EquipmentSlot.MAINHAND) && event.isCancelable()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void addRequirementListener(AddReloadListenerEvent event) {
        event.addListener(RequirementManager.instance = new RequirementManager());
    }

    @SubscribeEvent
    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        //synchronize Requirements
    }
}

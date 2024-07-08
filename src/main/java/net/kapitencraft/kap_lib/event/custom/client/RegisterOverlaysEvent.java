package net.kapitencraft.kap_lib.event.custom.client;

import net.kapitencraft.kap_lib.client.overlay.OverlayLocation;
import net.kapitencraft.kap_lib.client.overlay.PositionHolder;
import net.kapitencraft.kap_lib.client.overlay.holder.RenderHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * event class to register custom overlays to the OverlayManager for rendering and changing locations
 */
@OnlyIn(Dist.CLIENT)
public class RegisterOverlaysEvent extends Event implements IModBusEvent {
    private final BiConsumer<OverlayLocation, Function<PositionHolder, RenderHolder>> constructorFactory;

    public RegisterOverlaysEvent(BiConsumer<OverlayLocation, Function<PositionHolder, RenderHolder>> constructorFactory) {
        this.constructorFactory = constructorFactory;
    }

    /**
     * @param location the screen location the renderer should default to
     * @param constructor the constructor being called to create the Holder
     */
    public void addOverlay(OverlayLocation location, Function<PositionHolder, RenderHolder> constructor) {
        constructorFactory.accept(location, constructor);
    }
}

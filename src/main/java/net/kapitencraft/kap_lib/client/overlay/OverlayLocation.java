package net.kapitencraft.kap_lib.client.overlay;

import net.minecraft.MethodsReturnNonnullByDefault;

import java.util.UUID;

/**
 * a location interface
 */
@MethodsReturnNonnullByDefault
public interface OverlayLocation {
    /**
     * @return a persistent, unique UUID that's used for reading and saving possibly modified positions into - and from - JSON
     */
    UUID getUUID();

    /**
     * @return the default location where this Overlay should be
     */
    OverlayProperties getDefault();
}

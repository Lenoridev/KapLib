package net.kapitencraft.kap_lib.client.overlay;

import net.minecraft.MethodsReturnNonnullByDefault;

import java.util.UUID;

@MethodsReturnNonnullByDefault
public interface OverlayLocation {
    UUID getUUID();
    PositionHolder getDefault();
}

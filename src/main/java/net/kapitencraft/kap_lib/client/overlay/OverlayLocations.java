package net.kapitencraft.kap_lib.client.overlay;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * internal OverlayLocations
 */
public enum OverlayLocations implements OverlayLocation {
    STATS("cf4eb19d-aec8-4e65-8b43-c5e573b4561b", new OverlayProperties(-188.75f, 24f, .75f, .75f, OverlayProperties.Alignment.MIDDLE, OverlayProperties.Alignment.BOTTOM_RIGHT));


    private final UUID uuid;
    private final OverlayProperties holder;

    OverlayLocations(String stringUUID, OverlayProperties holder) {
        this.uuid = UUID.fromString(stringUUID);
        this.holder = holder;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull OverlayProperties getDefault() {
        return holder;
    }

    public static OverlayProperties getByUUID(UUID uuid) {
        return MiscHelper.getValue(OverlayLocations::getUUID, STATS, uuid, OverlayLocations.values()).getDefault();
    }
}

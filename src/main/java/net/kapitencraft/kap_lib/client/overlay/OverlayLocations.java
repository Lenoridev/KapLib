package net.kapitencraft.kap_lib.client.overlay;

import net.kapitencraft.kap_lib.helpers.MiscHelper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public enum OverlayLocations implements OverlayLocation {
    MANA("589c7ac3-4dc1-487b-b4b8-90524ce97bdc", new PositionHolder( 2f, 2, 1, 1, PositionHolder.Alignment.TOP_LEFT, PositionHolder.Alignment.TOP_LEFT)),
    STATS("cf4eb19d-aec8-4e65-8b43-c5e573b4561b", new PositionHolder(-220f, 22.5f, .75f, .75f, PositionHolder.Alignment.MIDDLE, PositionHolder.Alignment.BOTTOM_RIGHT));


    private final UUID uuid;
    private final PositionHolder holder;

    OverlayLocations(String stringUUID, PositionHolder holder) {
        this.uuid = UUID.fromString(stringUUID);
        this.holder = holder;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull PositionHolder getDefault() {
        return holder;
    }

    public static PositionHolder getByUUID(UUID uuid) {
        return MiscHelper.getValue(OverlayLocations::getUUID, MANA, uuid, OverlayLocations.values()).getDefault();
    }
}

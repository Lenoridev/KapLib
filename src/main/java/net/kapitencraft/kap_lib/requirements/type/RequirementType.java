package net.kapitencraft.kap_lib.requirements.type;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.function.ToIntFunction;

public abstract class RequirementType {
    private final ToIntFunction<Player> toId;
    protected final int minLevel;

    public RequirementType(ToIntFunction<Player> toId, int minLevel) {
        this.toId = toId;
        this.minLevel = minLevel;
    }

    public abstract Component display();

    public boolean matchesPlayer(Player player) {
        return minLevel <= toId.applyAsInt(player);
    }
}

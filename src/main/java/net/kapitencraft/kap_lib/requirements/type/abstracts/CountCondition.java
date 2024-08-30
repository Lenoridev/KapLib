package net.kapitencraft.kap_lib.requirements.type.abstracts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.ToIntFunction;

public abstract class CountCondition<T extends CountCondition<T>> extends ReqCondition<T> {
    private final ToIntFunction<LivingEntity> toId;
    protected final int minLevel;

    public CountCondition(ToIntFunction<LivingEntity> toId, int minLevel) {
        this.toId = toId;
        this.minLevel = minLevel;
    }




    protected abstract Component getCountedDisplay();

    @Override
    public @NotNull Component cacheDisplay() {
        return Component.translatable("item.requires", getCountedDisplay());
    }

    public boolean matches(LivingEntity player) {
        return minLevel <= toId.applyAsInt(player);
    }

    protected int getMinLevel() {
        return minLevel;
    }
}

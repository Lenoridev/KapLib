package net.kapitencraft.kap_lib.item.bonus;

import net.kapitencraft.kap_lib.cooldown.Cooldown;
import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class PieceBonus implements Bonus {
    private final String name;

    @Override
    public void onApply(LivingEntity living) {
    }

    @Override
    public void onUse() {
    }

    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public void onRemove(LivingEntity living) {
    }

    @Override
    public void onTick(Level level, @NotNull LivingEntity entity) {
    }

    @Override
    public void onEntityKilled(LivingEntity killed, LivingEntity user, MiscHelper.DamageType type) {
    }

    public PieceBonus(String name) {
        this.name = name;
    }

    @Override
    public String getSuperName() {
        return "Piece";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Consumer<List<Component>> getDisplay() {
        return list -> list.add(Component.literal("Piece Bonus: " + this.name).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
    }
}
package net.kapitencraft.kap_lib.util.attribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public class ChangingAttributeModifier extends AttributeModifier {
    private final LivingEntity living;
    private final Function<LivingEntity, Double> provider;

    public ChangingAttributeModifier(UUID p_22200_, String p_22201_, Operation p_22203_, LivingEntity living, Function<LivingEntity, Double> provider) {
        super(p_22200_, p_22201_, 0, p_22203_);
        this.living = living;
        this.provider = provider;
    }

    @Override
    public double getAmount() {
        return provider.apply(living);
    }

    @Override
    public @NotNull CompoundTag save() {
        throw new IllegalStateException("should not save changing attributeModifier");
    }
}

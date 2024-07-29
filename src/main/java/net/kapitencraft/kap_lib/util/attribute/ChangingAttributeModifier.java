package net.kapitencraft.kap_lib.util.attribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

/**
 * wrapper class for dynamic attribute modifiers that depend on a LivingEntity
 */
public class ChangingAttributeModifier extends AttributeModifier {
    private final LivingEntity living;
    private final Function<LivingEntity, Double> provider;

    public ChangingAttributeModifier(UUID id, String name, Operation operation, LivingEntity living, Function<LivingEntity, Double> provider) {
        super(id, name, 0, operation);
        this.living = living;
        this.provider = provider;
    }

    /**
     * @return the value provided by the {@link ChangingAttributeModifier#provider provider}
     */
    @Override
    public double getAmount() {
        return provider.apply(living);
    }

    @Override
    public @NotNull CompoundTag save() {
        throw new IllegalStateException("should not save changing attributeModifier");
    }
}

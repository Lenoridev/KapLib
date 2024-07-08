package net.kapitencraft.kap_lib.util.attribute;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimedModifier extends AttributeModifier {
    private int timer;
    private static final Map<LivingEntity, Multimap<Attribute, TimedModifier>> allModifiers = new HashMap<>();

    @SuppressWarnings("ALL")
    public static void tick(LivingEntity living) {
        allModifiers.get(living).forEach((attribute, timedModifier) -> {
                    if (timedModifier.tickDown()) living.getAttribute(attribute).removeModifier(timedModifier.getId());
                }
        );
    }

    private TimedModifier(String p_22201_, double p_22202_, Operation p_22203_, int timer) {
        super(UUID.randomUUID(), p_22201_, p_22202_, p_22203_);
        this.timer = timer;
    }

    public static TimedModifier addModifier(String name, double value, Operation operation, int time, LivingEntity living, Attribute attribute) {
        TimedModifier modifier = new TimedModifier(name, value, operation, time);
        allModifiers.get(living).put(attribute, modifier);
        return modifier;
    }

    private boolean tickDown() {
        return this.timer-- <= 0;
    }
}

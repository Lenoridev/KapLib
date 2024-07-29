package net.kapitencraft.kap_lib.event.custom;

import net.kapitencraft.kap_lib.requirements.RequirementManager;
import net.kapitencraft.kap_lib.requirements.RequirementType;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

public class RegisterRequirementTypesEvent extends Event {
    private final Consumer<RequirementType<?>> consumer;
    public RegisterRequirementTypesEvent(Consumer<RequirementType<?>> consumer) {
        this.consumer = consumer;
    }

    public void add(RequirementType<?> type) {
        consumer.accept(type);
    }
}

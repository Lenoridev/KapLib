package net.kapitencraft.kap_lib.cooldown;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class RepresentableMappedCooldown<T extends StringRepresentable> extends MappedCooldown<T> {
    public RepresentableMappedCooldown(String path, Consumer<LivingEntity> exe) {
        super(path, StringRepresentable::getSerializedName, exe);
    }
}

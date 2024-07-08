package net.kapitencraft.kap_lib.util.particle_help.animation.elements;

import net.kapitencraft.kap_lib.util.particle_help.animation.ParticleAnimator;
import net.minecraft.network.FriendlyByteBuf;

public abstract class ParticleAnimationElement {

    private final int amount;
    private final String id;

    public ParticleAnimationElement(int amount, String id) {
        this.amount = amount;
        this.id = id;
    }

    public abstract boolean tick(ParticleAnimator animator);

    protected final int getRotForIndex(int index) {
        return index * (360 / amount);
    }

    public String getId() {
        return id;
    }

    public abstract void writeToNW(FriendlyByteBuf buf);

    public abstract ParticleAnimationElement readFromNW(FriendlyByteBuf buf);

    public int getAmount() {
        return amount;
    }
}
package net.kapitencraft.kap_lib.util.particle_help.animation;

import net.kapitencraft.kap_lib.util.particle_help.animation.elements.ParticleAnimationElement;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ParticleAnimator {
    private final List<ParticleAnimationElement> elements = new ArrayList<>();

    public void addElement(ParticleAnimationElement element) {
        elements.add(element);
    }

    public void removeElement(ParticleAnimationElement element) {
        elements.remove(element);
    }

    public final Entity target;

    //TODO rewrite
    public ParticleAnimator(Entity target) {
        this.target = target;
    }

    public void tick() {
        if (this.target.isRemoved()) {
            return;
        }
        this.elements.removeIf((element) -> element.tick(this));
    }
}
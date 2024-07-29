package net.kapitencraft.kap_lib.client.particle.animation;

import net.kapitencraft.kap_lib.client.ClientData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * [WIP]
 */
@OnlyIn(Dist.CLIENT)
public class ParticleAnimationAcceptor {
    private final List<ParticleAnimationController> controllers = new ArrayList<>();

    public void addAnimation(ParticleAnimationController controller) {
        this.controllers.add(controller);
    }

    public void animateAll() {
        int time = ClientData.getTime();
        controllers.removeIf(controller -> {
            if (controller.isDone()) return true;
            if (controller.startTime == -1) {
                controller.startTime = time;
            }
            int activeTime = time - controller.startTime;
            controller.animate(activeTime);
            return controller.done(activeTime);
        });
    }
}
package net.kapitencraft.kap_lib.client.particle.animation;

import net.kapitencraft.kap_lib.helpers.MathHelper;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public enum ParticleAnimations implements Predicate<ParticleAnimationParameters>, BiConsumer<Particle, ParticleAnimationParameters> {
    MOVE_AWAY(ParticleAnimParams.SOURCE),
    MOVE_TO(ParticleAnimParams.TARGET),
    MOVE(ParticleAnimParams.DELTA),
    ROTATE(ParticleAnimParams.SOURCE, ParticleAnimParams.ROTATION);


    private final ParticleAnimParam<?>[] requiredParams;

    ParticleAnimations(ParticleAnimParam<?>... requiredParams) {
        this.requiredParams = requiredParams;
    }

    @Override
    public boolean test(ParticleAnimationParameters particleAnimationContext) {
        return particleAnimationContext.containsAll(requiredParams);
    }

    @SuppressWarnings("all")
    @Override
    public void accept(Particle particle, ParticleAnimationParameters params) {
        ParticleMoveControl control = ParticleMoveControl.fromParticle(particle);
        int remaining = params.remainingAnimTime;
        switch (this) {
            case MOVE -> {
                Vec3 moveVec = params.getParam(ParticleAnimParams.DELTA).orElse(Vec3.ZERO);
                control.deltaVec = moveVec;
            }
            case MOVE_TO -> {
                int id = params.getParamOrThrow(ParticleAnimParams.TARGET);
                Entity entity = control.level.getEntity(id);
                Vec3 move = control.loc.subtract(entity.position());
                control.deltaVec = control.loc.add(move.scale(move.length() / remaining));
            }
            case MOVE_AWAY -> {
                int id = params.getParamOrThrow(ParticleAnimParams.SOURCE);
                Entity entity = control.level.getEntity(id);
                Vec3 move = entity.position().subtract(control.loc);
                control.deltaVec = move.scale(remaining * 5);
            }
            case ROTATE -> {
                int id = params.getParamOrThrow(ParticleAnimParams.SOURCE);
                Entity entity = control.level.getEntity(id);
                int rot = params.getParamOrThrow(ParticleAnimParams.ROTATION);
                Vec3 outRot = MathHelper.calculateViewVector(0, rot);
                Vec3 dist = entity.position().subtract(control.loc);
            }
        }
        double particleSpeed = new Vec3(particle.xd, particle.yd, particle.zd).length();
        control.deltaVec = MathHelper.maximiseLength(control.deltaVec, particleSpeed);
        control.applyToParticle(particle);
    }
}
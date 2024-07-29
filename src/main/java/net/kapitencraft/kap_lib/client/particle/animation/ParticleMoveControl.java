package net.kapitencraft.kap_lib.client.particle.animation;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * [WIP]
 */
public class ParticleMoveControl {
    public Level level;
    Vec3 deltaVec;
    Vec3 loc;
    float r;
    float g;
    float b;
    float a;

    static ParticleMoveControl fromParticle(Particle particle) {
        ParticleMoveControl control = new ParticleMoveControl();
        control.deltaVec = new Vec3(particle.xd, particle.yd, particle.zd);
        control.loc = new Vec3(particle.x, particle.y, particle.z);
        control.r = particle.rCol;
        control.g = particle.gCol;
        control.b = particle.bCol;
        control.a = particle.alpha;
        control.level = particle.level;
        return control;
    }

    void applyToParticle(Particle target) {
        target.xd = deltaVec.x;
        target.yd = deltaVec.y;
        target.zd = deltaVec.z;
        target.x = loc.x;
        target.y = loc.y;
        target.z = loc.z;
        target.rCol = r;
        target.gCol = g;
        target.bCol = b;
        target.alpha = a;
    }
}
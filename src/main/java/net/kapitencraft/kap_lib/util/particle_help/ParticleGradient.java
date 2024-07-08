package net.kapitencraft.kap_lib.util.particle_help;

import net.minecraft.core.particles.ParticleOptions;

public class ParticleGradient {
    private final int maxSteps, particleCount;
    private final ParticleOptions firstParticle, secondParticle;
    private final int changePerStep;

    public ParticleGradient(int maxSteps, int particleCount, ParticleOptions firstParticle, ParticleOptions secondParticle) {
        this.maxSteps = maxSteps;
        this.particleCount = particleCount;
        this.firstParticle = firstParticle;
        this.secondParticle = secondParticle;
        this.changePerStep = particleCount / maxSteps;
    }

    public ParticleGradientHolder[] generate() {
        ParticleGradientHolder[] value = new ParticleGradientHolder[maxSteps];
        for (int i=0; i<maxSteps; i++) {
            int current1Particle = i * changePerStep;
            value[i] = new ParticleGradientHolder(new ParticleAmountHolder(firstParticle, current1Particle), new ParticleAmountHolder(secondParticle, particleCount - current1Particle));
        }
        return value;
    }
}

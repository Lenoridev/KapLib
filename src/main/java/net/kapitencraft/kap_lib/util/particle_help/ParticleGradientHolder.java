package net.kapitencraft.kap_lib.util.particle_help;

public final class ParticleGradientHolder {
    private final ParticleAmountHolder holder1;
    private final ParticleAmountHolder holder2;

    public ParticleGradientHolder(ParticleAmountHolder holder1, ParticleAmountHolder holder2) {
        this.holder1 = holder1;
        this.holder2 = holder2;
    }

    public ParticleAmountHolder getHolder1() {
        return holder1;
    }

    public ParticleAmountHolder getHolder2() {
        return holder2;
    }
}

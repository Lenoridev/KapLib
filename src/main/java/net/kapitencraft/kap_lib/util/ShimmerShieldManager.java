package net.kapitencraft.kap_lib.util;

import net.kapitencraft.kap_lib.client.particle.ShimmerShieldParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ShimmerShieldManager {
    private final HashMap<UUID, ShimmerShieldParticle> particles = new HashMap<>();

    public void removeShield(UUID uuid) {
        Objects.requireNonNull(this.particles.get(uuid), "no shield for UUID: " + uuid).remove();
    }

    public ShimmerShieldParticle addShield(UUID uuid, ShimmerShieldParticle particle) {
        if (particles.containsKey(uuid)) throw new IllegalStateException("shield for UUID " + uuid + " is already existing");
        particles.putIfAbsent(uuid, particle);
        return particle;
    }
}
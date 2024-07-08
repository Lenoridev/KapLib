package net.kapitencraft.kap_lib.util.particle_help.animation.elements;

import net.kapitencraft.kap_lib.helpers.MathHelper;
import net.kapitencraft.kap_lib.util.particle_help.animation.ParticleAnimator;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class OrbitAnimationElement extends ParticleAnimationElement {
    private final int rotPerTick, aliveTime;
    private int currentTick = 0, curRot = 0, cumTime = 0;
    private final ParticleOptions options;
    private final float heightChangePerTick, maxHeight;
    private float curHeightChange = 0;
    private boolean rising = true;

    public OrbitAnimationElement(String id, int amount, int rotPerTick, ParticleOptions options, int aliveTime, float heightChangePerTick, float maxHeight) {
        super(amount, id);
        this.rotPerTick = rotPerTick;
        this.options = options;
        this.aliveTime = aliveTime;
        this.heightChangePerTick = heightChangePerTick;
        this.maxHeight = maxHeight;
    }

    public OrbitAnimationElement(String id, int amount, int rotPerTick, ParticleOptions options, int aliveTime, float maxHeight) {
        this(id, amount, rotPerTick, options, aliveTime, (float) (0.5 * (360. / rotPerTick) * maxHeight), maxHeight);
    }


    @Override
    public boolean tick(ParticleAnimator animator) {
        if (this.currentTick++ >= 120) {
            this.currentTick = 0;
        }
        if (this.cumTime++ >= this.aliveTime) return true;
        Vec3 targetPos;
        if (animator.target instanceof Projectile) {
            targetPos = MathHelper.calculateViewVector(curRot, 0);
        } else {
            targetPos = MathHelper.calculateViewVector(0, curRot);
        }
        targetPos = MathHelper.setLength(targetPos, 0.75).add(animator.target.position()).add(0, curHeightChange, 0);
        animator.target.level().addParticle(options, targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);
        curRot += rotPerTick;
        applyHeightChange();
        return false;
    }

    @Override
    public void writeToNW(FriendlyByteBuf buf) {
        buf.writeUtf(getId());
        buf.writeInt(getAmount());
        buf.writeInt(rotPerTick);
        options.writeToNetwork(buf);
        buf.writeInt(aliveTime);
        buf.writeFloat(heightChangePerTick);
        buf.writeFloat(maxHeight);
    }

    @Override
    public ParticleAnimationElement readFromNW(FriendlyByteBuf buf) {
        return new OrbitAnimationElement(
                buf.readUtf(),
                buf.readInt(),
                buf.readInt(),
                null,
                buf.readInt(),
                buf.readFloat()
                );
    }

    private void applyHeightChange() {
        if (rising) {
            float distanceToMax = maxHeight - curHeightChange;
            if (distanceToMax < heightChangePerTick) {
                curHeightChange = maxHeight - (heightChangePerTick - distanceToMax);
                rising = false;
            } else {
                curHeightChange += heightChangePerTick;
            }
        } else {
            float distanceTo0 = curHeightChange;
            if (distanceTo0 < heightChangePerTick) {
                curHeightChange = 0 + (heightChangePerTick - distanceTo0);
                rising = true;
            } else {
                curHeightChange -= heightChangePerTick;
            }
        }
    }
}
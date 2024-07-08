package net.kapitencraft.kap_lib.client.particle.animation;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ParticleAnimationInfo implements Map<Integer, ParticleAnimations> {
    public static final ParticleAnimationInfo EMPTY = new ParticleAnimationInfo(Map.of());
    private final Map<Integer, ParticleAnimations> animationsForTime;

    public static ParticleAnimationInfo create(Map<Integer, ParticleAnimations> map) {
        return new ParticleAnimationInfo(map);
    }

    public ParticleAnimationInfo(Map<Integer, ParticleAnimations> animationsForTime) {
        this.animationsForTime = animationsForTime;
    }

    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeMap(animationsForTime, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeEnum);
    }

    public static ParticleAnimationInfo loadFromNetwork(FriendlyByteBuf buf) {
        return new ParticleAnimationInfo(buf.readMap(FriendlyByteBuf::readInt, buf1 -> buf1.readEnum(ParticleAnimations.class)));
    }

    @Override
    public int size() {
        return animationsForTime.size();
    }

    @Override
    public boolean isEmpty() {
        return animationsForTime.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return animationsForTime.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return animationsForTime.containsKey(value);
    }

    @Override
    public ParticleAnimations get(Object key) {
        return animationsForTime.get(key);
    }

    @Nullable
    @Override
    public ParticleAnimations put(Integer key, ParticleAnimations value) {
        return animationsForTime.put(key, value);
    }

    @Override
    public ParticleAnimations remove(Object key) {
        return animationsForTime.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends Integer, ? extends ParticleAnimations> m) {
        animationsForTime.putAll(m);
    }

    @Override
    public void clear() {
        animationsForTime.clear();
    }

    @NotNull
    @Override
    public Set<Integer> keySet() {
        return animationsForTime.keySet();
    }

    @NotNull
    @Override
    public Collection<ParticleAnimations> values() {
        return animationsForTime.values();
    }

    @NotNull
    @Override
    public Set<Entry<Integer, ParticleAnimations>> entrySet() {
        return animationsForTime.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return animationsForTime.equals(o);
    }

    @Override
    public int hashCode() {
        return animationsForTime.hashCode();
    }
}

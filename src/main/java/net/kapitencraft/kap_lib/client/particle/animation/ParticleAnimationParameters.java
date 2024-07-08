package net.kapitencraft.kap_lib.client.particle.animation;

import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.Markers;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ParticleAnimationParameters {
    int remainingAnimTime = -1;

    private final HashMap<ParticleAnimParam<?>, Object> params = new HashMap<>();

    public static ParticleAnimationParameters create() {
        return new ParticleAnimationParameters();
    }


    public <T> ParticleAnimationParameters withParam(ParticleAnimParam<T> paramKey, T paramValue) {
        params.put(paramKey, paramValue);
        return this;
    }

    public <T> boolean hasParam(ParticleAnimParam<T> key) {
        return params.containsKey(key);
    }

    public boolean containsAll(ParticleAnimParam<?>... keys) {
        return Arrays.stream(keys).allMatch(params::containsKey);
    }

    public <T> Optional<T> getParam(ParticleAnimParam<T> param) {
        if (params.containsKey(param)) {
            try {
                T t = (T) params.get(param);
                return Optional.of(t);
            } catch (ClassCastException e) {
                KapLibMod.LOGGER.warn((Marker) Markers.PARTICLE_ENGINE, "error getting interactive particle param: {}", e.getMessage());
            }
        }
        return Optional.empty();
    }

    public <T> T getParamOrThrow(ParticleAnimParam<T> param) {
        try {
            return (T) params.get(param);
        } catch (Exception e) {
            throw new NoSuchElementException("the animation parameters do not contain param " + param);
        }
    }

    public void writeToNetwork(FriendlyByteBuf buf) {
        params.forEach((interactiveParticleContextParam, o) ->
                interactiveParticleContextParam.accept(buf, o));
    }

    public static ParticleAnimationParameters loadFromNetwork(FriendlyByteBuf buf) {
        ParticleAnimationParameters context = new ParticleAnimationParameters();
        context.readFromNetwork(buf);
        return context;
    }

    public void readFromNetwork(FriendlyByteBuf buf) {
        while (true) {
            try {
                ParticleAnimParam<?> param = ParticleAnimParam.getParam(buf.readUtf());
                params.put(param, param.apply(buf));
            } catch (Exception e) {
                break;
            }
        }
    }
}
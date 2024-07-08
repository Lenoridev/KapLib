package net.kapitencraft.kap_lib.helpers;

import net.kapitencraft.kap_lib.collection.MapStream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorHelper {
    public static <T, L> Collector<T, ?, Map<T, L>> createMap(Function<T, L> valueMapper) {
        return Collectors.toMap(t -> t, valueMapper);
    }

    public static <T, L> Collector<L, ?, Map<T, L>> createMapForKeys(Function<L, T> keyMapper) {
        return Collectors.toMap(keyMapper, t-> t);
    }

    public static Collector<Component, MutableComponent, Component> joinComponent(Component filler) {
        return Collector.of(Component::empty, MutableComponent::append, (component, component2) -> {
            component.append(filler).append(component2);
            return component;
        }, Component::copy);
    }

    public static <T, K , L>  Collector<T, HashMap<K, L>, MapStream<K , L>> toMapStream(Function<T, K> keyMapper, Function<T, L> valueMapper) {
        return Collector.of(HashMap::new, (hashMap, t) -> hashMap.put(keyMapper.apply(t), valueMapper.apply(t)), CollectorHelper::mergeMap, MapStream::of);
    }

    public static <T, L>  Collector<T, HashMap<T, L>, MapStream<T , L>> toValueMappedStream(Function<T, L> valueMapper) {
        return Collector.of(HashMap::new, (hashMap, t) -> hashMap.put(t, valueMapper.apply(t)), CollectorHelper::mergeMap, MapStream::of);
    }

    public static <T, K> Collector<T, HashMap<K ,T>, MapStream<K, T>> toKeyMappedStream(Function<T, K> keyMapper) {
        return Collector.of(HashMap::new, (hashMap, t) -> hashMap.put(keyMapper.apply(t), t), CollectorHelper::mergeMap, MapStream::of);
    }

    private static <T, K> HashMap<T, K> mergeMap(HashMap<T, K> map, HashMap<T, K> map1) {
        map.putAll(map1);
        return map;
    }
}

package net.kapitencraft.kap_lib.collection;

import net.kapitencraft.kap_lib.helpers.CollectionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * class that contains a map of a map making it able to contain 3 different types
 */
public class DoubleMap<T, K, L> extends HashMap<T, Map<K, L>> {
    /**
     * used to make a double map immutable, crashing the game when trying to modify it
     */
    private boolean immutable = false;

    public void forValues(Consumer<L> consumer) {
        this.values().stream().map(Map::values).flatMap(Collection::stream).forEach(consumer);
    }

    public static <T, K, L> DoubleMap<T, K, L> of(Map<T, Map<K, L>> map) {
        DoubleMap<T, K, L> map1 = DoubleMap.create();
        map1.putAll(map);
        return map1;
    }

    public Map<K, L> getOrCreate(T element) {
        if (get(element) == null) {
            this.put(element, new HashMap<>());
        }
        return get(element);
    }

    public Collection<L> actualValues() {
        return CollectionHelper.values(this);
    }

    @Override
    public Map<K, L> put(T key, Map<K, L> value) {
        if (this.immutable) throw new UnsupportedOperationException("tried modifying immutable double map");
        return super.put(key, value);
    }

    public DoubleMap<T, K, L> immutable() {
        this.immutable = true;
        return this;
    }


    public void forMap(BiConsumer<K, L> biConsumer) {
        this.values().forEach(map -> map.forEach(biConsumer));
    }

    public static <T, K, L> DoubleMap<T, K, L> create() {
        return new DoubleMap<>();
    }

    public void put(T t, K k, L l) {
        if (this.immutable) throw new UnsupportedOperationException("tried modifying immutable double map");
        if (this.containsKey(t)) {
            this.get(t).put(k, l);
        } else {
            HashMap<K, L> map = new HashMap<>();
            map.put(k, l);
            this.put(t, map);
        }
    }

    public L getOrAdd(T t, K k, L ifAbsent) {
        if (this.immutable) throw new UnsupportedOperationException("tried modifying immutable double map");
        if (this.containsKey(t)) {
            this.get(t).putIfAbsent(k, ifAbsent);
        } else {
            this.put(t, new HashMap<>());
            this.get(t).put(k, ifAbsent);
        }
        return get(t, k);
    }

    public L get(T t, K k) {
        return this.get(t).get(k);
    }

    public L getOrNull(T t, K k) {
        try {
            return get(t, k);
        } catch (Exception e) {
            return null;
        }
    }
}

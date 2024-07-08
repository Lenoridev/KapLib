package net.kapitencraft.kap_lib.helpers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class CollectionHelper {

    public static <T, K> T getFirstKey(Map<T, K> collection) {
        return collection.keySet().stream().findFirst().get();
    }

    public static <T, K> T getKeyForValue(Map<T, K> map, K value) {
        for (Map.Entry<T, K> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return null;
    }

    public static <T> @Nullable T getOptionalOrNull(Optional<T> optional) {
        return optional.orElse(null);
    }

    public static <T, K> Map<K, T> mirror(Map<T, K> in) {
        Map<K, T> returnMap = new HashMap<>();
        in.forEach((t, k) -> returnMap.put(k, t));
        return returnMap;
    }

    public static <T, K> Multimap<T, K> sortMap(Multimap<T, K> map, @Nullable Comparator<T> keySorter, @Nullable Comparator<K> valueSorter) {
        List<T> sortedKeys = fromAny(map.keySet());
        MiscHelper.ifNonNull(keySorter, sortedKeys::sort);
        Multimap<T, K> multimap = HashMultimap.create();
        sortedKeys.forEach(t -> {
            List<K> values = mutableList(fromAny(map.get(t)));
            MiscHelper.ifNonNull(valueSorter, values::sort);
            multimap.putAll(t, values);
        });
        return multimap;
    }

    public static <T> List<T> mutableList(List<T> immutable) {
        return new ArrayList<>(immutable);
    }

    public static <T> List<T> fromAny(Collection<T> collection) {
        return collection.stream().toList();
    }

    public static <T> void removeMapping2(List<T> ts, BiPredicate<T, T> predicate) {
        ts.removeIf(t -> {
            int id = ts.indexOf(t);
            T second;
            if (id < ts.size() - 1) {
                second = ts.get(id + 1);
            } else {
                second = ts.get(0);
            }
            return predicate.test(t, second);
        });
    }

    public static <T> void forEachMapping2(List<T> ts, BiConsumer<T, T> consumer) {
        ts.forEach(t -> {
            int id = ts.indexOf(t);
            T second;
            if (id < ts.size() - 1) {
                second = ts.get(id + 1);
            } else {
                second = ts.get(0);
            }
            consumer.accept(t, second);
        });
    }

    public static <T> List<T> create(int size, Supplier<T> sup) {
        List<T> list = new ArrayList<>();
        MiscHelper.repeat(size, integer -> list.add(sup.get()));
        return list;
    }

    public static <T, K> Multimap<T, K> fromMap(Map<T, K> map) {
        Multimap<T, K> multimap = HashMultimap.create();
        for (T t : map.keySet()) {
            multimap.put(t, map.get(t));
        }
        return multimap;
    }

    public static <T> ArrayList<T> toList(T ts) {
        ArrayList<T> target = new ArrayList<>();
        Collections.addAll(target, ts);
        return target;
    }


    public static <T, K, L, J extends Map<K, L>> List<L> values(Map<T, J> map) {
        return map.values().stream().map(Map::values).flatMap(Collection::stream).toList();
    }

    public static <T, K> K getFirstValue(Map<T, K> map) {
        for (Map.Entry<T, K> entry : map.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    public static <T, K> void removeIf(Map<T, K> map, Predicate<T> predicate) {
        for (T t : map.keySet()) {
            if (predicate.test(t)) {
                map.remove(t);
            }
        }
    }

    public static <T, V> void forEach(HashMap<T, HashMap<T, V>> map, BiConsumer<? super T, ? super V> consumer) {
        for (HashMap<T, V> map1 : map.values()) {
            map1.forEach(consumer);
        }
    }

    public static  <V> ArrayList<V> invertList(ArrayList<V> list) {
        ArrayList<V> out = new ArrayList<>();
        for (int i = list.size(); i > 0; i--) {

            out.add(list.get((i - 1)));
        }
        return out;
    }

    public static List<LivingEntity> sortLowestDistance(Entity source, List<LivingEntity> list) {
        if (list.isEmpty()) {
            return List.of();
        }
        return list.stream().sorted(Comparator.comparingDouble(living -> living.distanceToSqr(source.position()))).collect(Collectors.toList());
    }

    public static <T> boolean arrayContains(T[] array, T t) {
        return List.of(array).contains(t);
    }

    public static <T> T[] listToArray(List<T> list) {
        return (T[]) list.toArray();
    }

    public static <T> List<T> copy(T[] source) {
        return Arrays.asList(source);
    }

    public static <T> List<T> remove(T[] values, T... toRemove) {
        return Arrays.stream(values).filter(t -> !arrayContains(toRemove, t)).toList();
    }

    public static <T> List<T> add(T[] values, T... toAdd) {
        List<T> source = new ArrayList<>(Arrays.stream(values).toList());
        source.addAll(Arrays.stream(toAdd).toList());
        return source;
    }
}

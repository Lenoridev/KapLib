package net.kapitencraft.kap_lib.registry.custom;

import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.io.network.request.IRequestable;
import net.kapitencraft.kap_lib.requirements.type.abstracts.ReqCondition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;


public interface ModRegistryKeys {

    ResourceKey<Registry<IRequestable<?, ?>>> REQUESTABLES = createRegistry("requestables");
    ResourceKey<Registry<Codec<? extends ReqCondition<?>>>> REQ_CONDITIONS = createRegistry("requirements");

    private static <T> ResourceKey<Registry<T>> createRegistry(String id) {
        return ResourceKey.createRegistryKey(KapLibMod.res(id));
    }
}

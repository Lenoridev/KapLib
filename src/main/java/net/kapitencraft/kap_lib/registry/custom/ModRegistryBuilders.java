package net.kapitencraft.kap_lib.registry.custom;

import net.kapitencraft.kap_lib.io.network.request.IRequestable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryBuilder;

public class ModRegistryBuilders {

    public static final RegistryBuilder<IRequestable<?, ?>> REQUESTABLES_BUILDER = makeBuilder(ModRegistryKeys.REQUESTABLES);

    private static <T> RegistryBuilder<T> makeBuilder(ResourceKey<Registry<T>> location) {
        return new RegistryBuilder<T>().setName(location.location());
    }
}

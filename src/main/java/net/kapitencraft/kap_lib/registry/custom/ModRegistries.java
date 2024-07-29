package net.kapitencraft.kap_lib.registry.custom;

import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.io.network.request.IRequestable;
import net.kapitencraft.kap_lib.requirements.type.abstracts.ReqCondition;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public interface ModRegistries {
    IForgeRegistry<IRequestable<?, ?>> REQUESTABLES = RegistryManager.ACTIVE.getRegistry(ModRegistryKeys.REQUESTABLES);
    IForgeRegistry<Codec<? extends ReqCondition<?>>> REQUIREMENT_TYPE = RegistryManager.ACTIVE.getRegistry(ModRegistryKeys.REQ_CONDITIONS);
}

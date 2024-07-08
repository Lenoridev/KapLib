package net.kapitencraft.kap_lib.registry.custom;

import net.kapitencraft.kap_lib.io.network.request.IRequestable;
import net.kapitencraft.kap_lib.requirements.Requirement;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public interface ModRegistries {
    IForgeRegistry<IRequestable<?, ?>> REQUESTABLES_REGISTRY = RegistryManager.ACTIVE.getRegistry(ModRegistryKeys.REQUESTABLES);
    IForgeRegistry<Requirement<?>> REQUIREMENT_REGISTRY = RegistryManager.ACTIVE.getRegistry(ModRegistryKeys.REQUIREMENTS);
}

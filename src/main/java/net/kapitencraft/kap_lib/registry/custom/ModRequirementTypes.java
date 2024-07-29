package net.kapitencraft.kap_lib.registry.custom;

import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.KapLibMod;
import net.kapitencraft.kap_lib.requirements.type.DimensionReqCondition;
import net.kapitencraft.kap_lib.requirements.type.abstracts.CountCondition;
import net.kapitencraft.kap_lib.requirements.type.StatReqCondition;
import net.kapitencraft.kap_lib.requirements.type.abstracts.ReqCondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public interface ModRequirementTypes {

    DeferredRegister<Codec<? extends ReqCondition<?>>> REGISTRY = KapLibMod.registry(ModRegistryKeys.REQ_CONDITIONS);

    RegistryObject<Codec<StatReqCondition>> STAT_REQ = REGISTRY.register("stat_req", () -> StatReqCondition.CODEC);
    RegistryObject<Codec<DimensionReqCondition>> DIMENSION = REGISTRY.register("dimension", () -> DimensionReqCondition.CODEC);
}

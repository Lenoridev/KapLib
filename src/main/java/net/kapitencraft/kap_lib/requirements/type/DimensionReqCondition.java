package net.kapitencraft.kap_lib.requirements.type;

import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.requirements.type.abstracts.CountCondition;
import net.kapitencraft.kap_lib.requirements.type.abstracts.ReqCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

public class DimensionReqCondition extends ReqCondition<DimensionReqCondition> {
    public static final Codec<DimensionReqCondition> CODEC = ResourceKey.codec(Registries.DIMENSION).xmap(DimensionReqCondition::new, DimensionReqCondition::getDimensionKey);

    private final ResourceKey<Level> dimensionKey;

    private ResourceKey<Level> getDimensionKey() {
        return dimensionKey;
    }

    public DimensionReqCondition(ResourceKey<Level> dimensionKey) {
        this.dimensionKey = dimensionKey;
    }

    @Override
    public Component display() {
        return Component.translatable("dimension_req.display", Component.translatable("dimension." + dimensionKey.location().toString().replace(':', '.')));
    }

    @Override
    public boolean matches(Player player) {
        return player.level().dimension() == dimensionKey;
    }

    @Override
    public Codec<DimensionReqCondition> getCodec() {
        return CODEC;
    }
}

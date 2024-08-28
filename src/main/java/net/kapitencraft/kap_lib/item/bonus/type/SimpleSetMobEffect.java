package net.kapitencraft.kap_lib.item.bonus.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kapitencraft.kap_lib.io.serialization.DataGenSerializer;
import net.kapitencraft.kap_lib.item.bonus.Bonus;
import net.kapitencraft.kap_lib.registry.custom.ModSetBonusTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleSetMobEffect implements Bonus<SimpleSetMobEffect> {
    private static final Codec<MobEffectInstance> EFFECT_INSTANCE_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
                    Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
                    Codec.INT.fieldOf("amplifier").forGetter(MobEffectInstance::getAmplifier)
            ).apply(instance, MobEffectInstance::new)
    );

    public static final Codec<SimpleSetMobEffect> CODEC = EFFECT_INSTANCE_CODEC.listOf().xmap(SimpleSetMobEffect::new, SimpleSetMobEffect::getEffects);

    public SimpleSetMobEffect(List<MobEffectInstance> effects) {
        this.effects.addAll(effects);
    }

    private List<MobEffectInstance> getEffects() {
        return effects;
    }

    private final List<MobEffectInstance> effects = new ArrayList<>();

    @Override
    public DataGenSerializer<SimpleSetMobEffect> getSerializer() {
        return ModSetBonusTypes.SIMPLE_MOB_EFFECT.get();
    }

    @Override
    public void additionalToNetwork(FriendlyByteBuf buf) {
        buf.writeCollection(this.effects, SimpleSetMobEffect::writeEffect);
    }

    @Override
    public void onTick(Level level, @NotNull LivingEntity entity) {
        this.effects.stream()
                .map(MobEffectInstance::new) //create new Object to effectively finalize 'effects'
                .forEach(entity::addEffect);
    }

    @Override
    public Consumer<List<Component>> getDisplay() {
        return null;
    }

    private static void writeEffect(FriendlyByteBuf buf, MobEffectInstance instance) {
        buf.writeRegistryId(ForgeRegistries.MOB_EFFECTS, instance.getEffect());
        buf.writeInt(instance.getDuration());
        buf.writeInt(instance.getAmplifier());
        //other information ignored
    }

    public static SimpleSetMobEffect fromNetwork(FriendlyByteBuf buf) {
        return new SimpleSetMobEffect(buf.readCollection(ArrayList::new, SimpleSetMobEffect::readEffect));
    }

    private static MobEffectInstance readEffect(FriendlyByteBuf buf) {
        MobEffect effect = buf.readRegistryId();
        int duration = buf.readInt();
        int amplifier = buf.readInt();
        return new MobEffectInstance(effect, duration, amplifier);
    }
}

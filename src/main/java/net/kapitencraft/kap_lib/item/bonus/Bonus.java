package net.kapitencraft.kap_lib.item.bonus;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.cooldown.Cooldown;
import net.kapitencraft.kap_lib.helpers.MiscHelper;
import net.kapitencraft.kap_lib.io.serialization.DataGenSerializer;
import net.kapitencraft.kap_lib.io.serialization.IDataGenElement;
import net.kapitencraft.kap_lib.item.IEventListener;
import net.kapitencraft.kap_lib.registry.custom.core.ModRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface Bonus<T extends Bonus<T>> extends IDataGenElement<T>, IEventListener {

    static <T extends Bonus<T>> DataGenSerializer<T> createSerializer(Codec<T> codec, FriendlyByteBuf.Reader<T> factory) {
        return IDataGenElement.createSerializer(codec, factory);
    }

    @Override
    default void toNetwork(FriendlyByteBuf buf) {
        buf.writeRegistryId(ModRegistries.BONUS_SERIALIZER, this.getSerializer());
        additionalToNetwork(buf);
    }

    @Override
    default JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("data", getSerializer().serialize((T) this));
        object.addProperty("type", Objects.requireNonNull(ModRegistries.BONUS_SERIALIZER.getKey(this.getSerializer()), String.format("unknown requirement type: %s", this.getClass().getCanonicalName())).toString());
        return object;
    }

    default void onApply(LivingEntity living) {
    }

    default void onUse() {
    }

    DataGenSerializer<T> getSerializer();

    @org.jetbrains.annotations.Nullable
    default Cooldown getCooldown() {
        return null;
    }

    default void onTick(Level level, @NotNull LivingEntity entity) {
    }

    default void onEntityKilled(LivingEntity killed, LivingEntity user, MiscHelper.DamageType type) {
    }

    default void onRemove(LivingEntity living) {
    }

    default @Nullable Multimap<Attribute, AttributeModifier> getModifiers(LivingEntity living) {return null;}

    default float onEntityHurt(LivingEntity hurt, LivingEntity user, MiscHelper.DamageType type, float damage) {
        return damage;
    }

    default float onTakeDamage(LivingEntity hurt, LivingEntity source, MiscHelper.DamageType type, float damage) {
        return damage;
    }
    Consumer<List<Component>> getDisplay();

    default String getSuperName() {
        return "Bonus";
    }


    default List<Component> makeDisplay() {
        List<Component> display = new ArrayList<>();
        display.add(Component.literal( getSuperName() + ": ").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
        getDisplay().accept(display);
        display.add(CommonComponents.EMPTY);
        return display;
    }
}
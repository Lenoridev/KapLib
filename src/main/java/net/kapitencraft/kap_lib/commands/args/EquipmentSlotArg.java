package net.kapitencraft.kap_lib.commands.args;

import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EquipmentSlotArg extends SimpleEnumArg<EquipmentSlot> {

    protected EquipmentSlotArg() {
        super(EquipmentSlot::getName, EquipmentSlot.values());
    }

    public static EquipmentSlot getSlot(CommandContext<CommandSourceStack> stack, String name) {
        return stack.getArgument(name, EquipmentSlot.class);
    }

    public static EquipmentSlotArg slot() {
        return new EquipmentSlotArg();
    }

    public static class Info implements ArgumentTypeInfo<EquipmentSlotArg, Info.Template> {

        @Override
        public void serializeToNetwork(Template p_235375_, FriendlyByteBuf p_235376_) {

        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf p_235377_) {
            return new Template();
        }

        @Override
        public void serializeToJson(Template p_235373_, JsonObject p_235374_) {

        }

        @Override
        public Template unpack(EquipmentSlotArg p_235372_) {
            return new Template();
        }

        public class Template implements ArgumentTypeInfo.Template<EquipmentSlotArg> {

            @Override
            public EquipmentSlotArg instantiate(CommandBuildContext p_235378_) {
                return new EquipmentSlotArg();
            }

            @Override
            public ArgumentTypeInfo<EquipmentSlotArg, ?> type() {
                return Info.this;
            }
        }
    }
}
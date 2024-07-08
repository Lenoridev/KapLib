package net.kapitencraft.kap_lib.io.network.request;

import net.minecraft.network.FriendlyByteBuf;

public record SimpleSuccessResult(String id, boolean success) {

    public void save(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeBoolean(success);
    }

    public static SimpleSuccessResult load(FriendlyByteBuf buf) {
        return new SimpleSuccessResult(buf.readUtf(), buf.readBoolean());
    }

    public static SimpleSuccessResult fail(String message) {
        return new SimpleSuccessResult(message, false);
    }

    public static SimpleSuccessResult success(String message) {
        return new SimpleSuccessResult(message, true);
    }
}

package net.kapitencraft.kap_lib.event.custom;

import net.kapitencraft.kap_lib.io.network.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

public class RegisterRequestEvent extends Event implements IModBusEvent {
    private final SimpleChannel handler;
    private int id;

    public RegisterRequestEvent(SimpleChannel handler, int id) {
        this.handler = handler;
        this.id = id;
    }

    private int id() {
        return id++;
    }


    private <T extends SimplePacket> void addMessage(Class<T> tClass, NetworkDirection direction, Function<FriendlyByteBuf, T> decoder) {
        handler.messageBuilder(tClass, id(), direction)
                .decoder(decoder)
                .encoder(T::toBytes)
                .consumerMainThread(T::handle)
                .add();
    }

    private <T extends SimplePacket> void addSimpleMessage(Class<T> packetClass, NetworkDirection direction, Supplier<T> supplier) {
        addMessage(packetClass, direction, buf -> supplier.get());
    }
}

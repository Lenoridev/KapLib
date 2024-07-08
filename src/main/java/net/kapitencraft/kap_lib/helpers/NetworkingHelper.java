package net.kapitencraft.kap_lib.helpers;

import net.kapitencraft.kap_lib.io.network.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class NetworkingHelper {

    public static void writeVec3(FriendlyByteBuf buf, Vec3 vec3) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    public static <T> void writeArray(FriendlyByteBuf buf, T[] array, FriendlyByteBuf.Writer<T> writer) {
        buf.writeInt(array.length);
        for (T t : array) {
            writer.accept(buf, t);
        }
    }

    public static <T> T[] readArray(FriendlyByteBuf buf, IntFunction<T[]> constructor, FriendlyByteBuf.Reader<T> reader) {
        int length = buf.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(reader.apply(buf));
        }
        return list.toArray(constructor);
    }

    public static Vec3 readVec3(FriendlyByteBuf buf) {
        return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void writeVector3f(FriendlyByteBuf buf, Vector3f vec) {
        buf.writeFloat(vec.x);
        buf.writeFloat(vec.y);
        buf.writeFloat(vec.z);
    }
}

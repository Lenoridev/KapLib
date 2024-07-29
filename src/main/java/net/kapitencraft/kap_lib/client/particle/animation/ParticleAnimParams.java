package net.kapitencraft.kap_lib.client.particle.animation;

import net.kapitencraft.kap_lib.helpers.NetworkingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

/**
 * [WIP]
 */
public class ParticleAnimParams {
    public static final ParticleAnimParam<Vec3> DELTA = new ParticleAnimParam<>("delta", NetworkingHelper::writeVec3, NetworkingHelper::readVec3);
    public static final ParticleAnimParam<Integer> SOURCE = new ParticleAnimParam<>("source", FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
    public static final ParticleAnimParam<Integer> TARGET = new ParticleAnimParam<>("target", SOURCE::accept, SOURCE);
    public static final ParticleAnimParam<Boolean> LOOP = new ParticleAnimParam<>("loop", FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    public static final ParticleAnimParam<Integer> ROTATION = new ParticleAnimParam<>("rotation", FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
}
package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.packets.EntityHeadRotationPacket;

import javax.annotation.Nonnull;

public final class NMSEntityHeadRotationPacket extends EntityHeadRotationPacket {

    NMSEntityHeadRotationPacket(int entityId, float yaw) {
        super(entityId, yaw);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityHeadRotation build() {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(PacketManager.SERIALIZER);
        Reflection.setField(packet, "a", getEntityId());
        Reflection.setField(packet, "b", (byte) (getYaw() * 256 / 360));
        return packet;
    }
}

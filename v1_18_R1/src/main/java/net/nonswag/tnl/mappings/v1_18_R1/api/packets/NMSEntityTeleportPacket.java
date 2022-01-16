package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.location.Position;
import net.nonswag.tnl.listener.api.packets.EntityTeleportPacket;

import javax.annotation.Nonnull;

public final class NMSEntityTeleportPacket extends EntityTeleportPacket {

    NMSEntityTeleportPacket(int entityId, @Nonnull Position position) {
        super(entityId, position);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityTeleport build() {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(PacketManager.SERIALIZER);
        Reflection.setField(packet, "a", getEntityId());
        Reflection.setField(packet, "b", getPosition().getX());
        Reflection.setField(packet, "c", getPosition().getY());
        Reflection.setField(packet, "d", getPosition().getZ());
        Reflection.setField(packet, "e", (byte) ((int) (getPosition().getYaw() * 256.0F / 360.0F)));
        Reflection.setField(packet, "f", (byte) ((int) (getPosition().getPitch() * 256.0F / 360.0F)));
        Reflection.setField(packet, "g", false);
        return packet;
    }
}

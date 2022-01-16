package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.packets.EntityStatusPacket;

import javax.annotation.Nonnull;

public final class NMSEntityStatusPacket extends EntityStatusPacket {

    NMSEntityStatusPacket(int entityId, @Nonnull Status status) {
        super(entityId, status);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityStatus build() {
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(PacketManager.SERIALIZER);
        Reflection.setField(packet, "a", getEntityId());
        Reflection.setField(packet, "b", getStatus().getId());
        return packet;
    }
}

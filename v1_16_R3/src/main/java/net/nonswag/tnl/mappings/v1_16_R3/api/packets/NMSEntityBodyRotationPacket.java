package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.nonswag.tnl.listener.api.packets.EntityBodyRotationPacket;

import javax.annotation.Nonnull;

import static net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;

public final class NMSEntityBodyRotationPacket extends EntityBodyRotationPacket {

    NMSEntityBodyRotationPacket(int entityId, float rotation) {
        super(entityId, rotation);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityLook build() {
        return new PacketPlayOutEntityLook(getEntityId(), (byte) getRotation(), (byte) 0, true);
    }
}

package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.nonswag.tnl.listener.api.packets.EntityAnimationPacket;
import net.nonswag.tnl.listener.api.serializer.PacketSerializer;

import javax.annotation.Nonnull;

public final class NMSEntityAnimationPacket extends EntityAnimationPacket {

    NMSEntityAnimationPacket(int entityId, @Nonnull EntityAnimationPacket.Animation animation) {
        super(entityId, animation);
    }

    @Nonnull
    @Override
    public PacketPlayOutAnimation build() {
        PacketSerializer serializer = new PacketSerializer(getEntityId());
        serializer.writeByte((byte) getAnimation().getId());
        return new PacketPlayOutAnimation(new PacketDataSerializer(serializer.getBuf()));
    }
}

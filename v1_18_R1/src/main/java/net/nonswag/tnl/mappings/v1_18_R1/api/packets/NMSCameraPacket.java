package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.nonswag.tnl.listener.api.packets.CameraPacket;
import net.nonswag.tnl.listener.api.serializer.PacketSerializer;

import javax.annotation.Nonnull;

public final class NMSCameraPacket extends CameraPacket {

    NMSCameraPacket(int targetId) {
        super(targetId);
    }

    @Nonnull
    @Override
    public PacketPlayOutCamera build() {
        PacketSerializer serializer = new PacketSerializer(getTargetId());
        return new PacketPlayOutCamera(new PacketDataSerializer(serializer.getBuf()));
    }
}

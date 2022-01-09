package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutCamera;
import net.nonswag.tnl.listener.api.packets.CameraPacket;

import javax.annotation.Nonnull;

public final class NMSCameraPacket extends CameraPacket {

    NMSCameraPacket(int targetId) {
        super(targetId);
    }

    @Nonnull
    @Override
    public PacketPlayOutCamera build() {
        PacketPlayOutCamera packet = new PacketPlayOutCamera();
        packet.a = getTargetId();
        return packet;
    }
}

package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.packets.MountPacket;

import javax.annotation.Nonnull;

public final class NMSMountPacket extends MountPacket {

    NMSMountPacket(int holderId, int[] mounts) {
        super(holderId, mounts);
    }

    @Nonnull
    @Override
    public PacketPlayOutMount build() {
        PacketPlayOutMount packet = new PacketPlayOutMount(PacketManager.SERIALIZER);
        Reflection.setField(packet, "a", getHolderId());
        Reflection.setField(packet, "b", getMounts());
        return packet;
    }
}

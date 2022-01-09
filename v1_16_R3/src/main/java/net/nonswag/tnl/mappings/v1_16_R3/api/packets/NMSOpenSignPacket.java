package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor;
import net.nonswag.tnl.listener.api.location.BlockLocation;
import net.nonswag.tnl.listener.api.packets.OpenSignPacket;

import javax.annotation.Nonnull;

public final class NMSOpenSignPacket extends OpenSignPacket {

    NMSOpenSignPacket(@Nonnull BlockLocation location) {
        super(location);
    }

    @Nonnull
    @Override
    public PacketPlayOutOpenSignEditor build() {
        return new PacketPlayOutOpenSignEditor(new BlockPosition(getLocation().getX(), getLocation().getY(), getLocation().getZ()));
    }
}

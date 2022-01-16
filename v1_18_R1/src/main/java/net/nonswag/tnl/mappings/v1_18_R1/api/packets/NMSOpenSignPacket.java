package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
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

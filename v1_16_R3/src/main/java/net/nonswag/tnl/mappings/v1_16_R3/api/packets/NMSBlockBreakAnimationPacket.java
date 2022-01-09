package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockBreakAnimation;
import net.nonswag.tnl.listener.api.location.BlockLocation;
import net.nonswag.tnl.listener.api.packets.BlockBreakAnimationPacket;

import javax.annotation.Nonnull;

public final class NMSBlockBreakAnimationPacket extends BlockBreakAnimationPacket {

    NMSBlockBreakAnimationPacket(@Nonnull BlockLocation location, int state) {
        super(location, state);
    }

    @Nonnull
    @Override
    public PacketPlayOutBlockBreakAnimation build() {
        BlockPosition position = new BlockPosition(getLocation().getX(), getLocation().getY(), getLocation().getZ());
        return new PacketPlayOutBlockBreakAnimation(getLocation().getBlock().hashCode(), position, getState());
    }
}

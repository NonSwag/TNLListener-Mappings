package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import net.nonswag.tnl.listener.api.border.VirtualBorder;
import net.nonswag.tnl.listener.api.packets.WorldBorderPacket;

import javax.annotation.Nonnull;

public final class NMSWorldBorderPacket extends WorldBorderPacket {

    NMSWorldBorderPacket(@Nonnull VirtualBorder virtualBorder, @Nonnull Action action) {
        super(virtualBorder, action);
    }

    @Nonnull
    @Override
    public ClientboundInitializeBorderPacket build() {
        return new ClientboundInitializeBorderPacket(new WorldBorder());
    }
}

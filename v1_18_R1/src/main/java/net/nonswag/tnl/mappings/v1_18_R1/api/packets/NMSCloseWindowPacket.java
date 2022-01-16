package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.nonswag.tnl.listener.api.packets.CloseWindowPacket;

import javax.annotation.Nonnull;

public final class NMSCloseWindowPacket extends CloseWindowPacket {

    NMSCloseWindowPacket(int windowId) {
        super(windowId);
    }

    @Nonnull
    @Override
    public PacketPlayOutCloseWindow build() {
        return new PacketPlayOutCloseWindow(getWindowId());
    }
}

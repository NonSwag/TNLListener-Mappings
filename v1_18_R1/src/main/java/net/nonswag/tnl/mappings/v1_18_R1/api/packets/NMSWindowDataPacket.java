package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutWindowData;
import net.nonswag.tnl.listener.api.packets.WindowDataPacket;

import javax.annotation.Nonnull;

public final class NMSWindowDataPacket extends WindowDataPacket {

    NMSWindowDataPacket(int windowId, int property, int value) {
        super(windowId, property, value);
    }

    @Nonnull
    @Override
    public PacketPlayOutWindowData build() {
        return new PacketPlayOutWindowData(getWindowId(), getProperty(), getValue());
    }
}

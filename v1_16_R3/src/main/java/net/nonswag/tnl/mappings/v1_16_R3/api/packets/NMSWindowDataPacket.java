package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutWindowData;
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

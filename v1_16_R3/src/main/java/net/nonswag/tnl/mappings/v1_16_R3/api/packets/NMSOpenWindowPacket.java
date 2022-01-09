package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.packets.OpenWindowPacket;

import javax.annotation.Nonnull;

public final class NMSOpenWindowPacket extends OpenWindowPacket {

    NMSOpenWindowPacket(int windowId, @Nonnull Type type, @Nonnull String title) {
        super(windowId, type, title);
    }

    @Nonnull
    @Override
    public PacketPlayOutOpenWindow build() {
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow();
        IChatBaseComponent[] msg = org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage.fromString(getTitle());
        if (msg.length >= 1) Reflection.setField(packet, "c", msg[0]);
        else Reflection.setField(packet, "c", new net.minecraft.server.v1_16_R3.ChatMessage(getTitle()));
        Reflection.setField(packet, "a", getWindowId());
        Reflection.setField(packet, "b", getType().getId());
        return packet;
    }
}

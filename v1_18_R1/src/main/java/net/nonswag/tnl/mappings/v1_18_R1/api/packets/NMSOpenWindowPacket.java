package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
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
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(PacketManager.SERIALIZER);
        IChatBaseComponent[] msg = org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage.fromString(getTitle());
        if (msg.length >= 1) Reflection.setField(packet, "c", msg[0]);
        else Reflection.setField(packet, "c", new ChatMessage(getTitle()));
        Reflection.setField(packet, "a", getWindowId());
        Reflection.setField(packet, "b", getType().getId());
        return packet;
    }
}

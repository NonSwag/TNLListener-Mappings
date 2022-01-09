package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.nonswag.tnl.listener.api.packets.ChatPacket;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class NMSChatPacket extends ChatPacket {

    NMSChatPacket(@Nonnull String message, @Nonnull Type type, @Nonnull UUID sender) {
        super(message, type, sender);
    }

    @Nonnull
    @Override
    public PacketPlayOutChat build() {
        IChatBaseComponent[] msg = CraftChatMessage.fromString(getMessage());
        if (msg.length > 0) return new PacketPlayOutChat(msg[0], type(), getSender());
        return new PacketPlayOutChat(new ChatMessage(getMessage()), type(), getSender());
    }

    @Nonnull
    private ChatMessageType type() {
        return switch (getType()) {
            case CHAT -> ChatMessageType.CHAT;
            case GAME_INFO -> ChatMessageType.GAME_INFO;
            case SYSTEM -> ChatMessageType.SYSTEM;
        };
    }
}

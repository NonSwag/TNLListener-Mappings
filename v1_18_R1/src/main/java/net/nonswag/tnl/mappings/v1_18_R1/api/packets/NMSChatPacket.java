package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.nonswag.tnl.listener.api.packets.ChatPacket;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;

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
            case CHAT -> ChatMessageType.a;
            case SYSTEM -> ChatMessageType.b;
            case GAME_INFO -> ChatMessageType.c;
        };
    }
}

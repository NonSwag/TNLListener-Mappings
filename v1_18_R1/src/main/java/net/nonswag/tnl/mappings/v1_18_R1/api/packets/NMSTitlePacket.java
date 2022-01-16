package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.nonswag.tnl.listener.api.packets.TitlePacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class NMSTitlePacket extends TitlePacket {

    NMSTitlePacket(@Nonnull Action action, @Nullable String text, int timeIn, int timeStay, int timeOut) {
        super(action, text, timeIn, timeStay, timeOut);
    }

    @Nonnull
    @Override
    public ClientboundSetTitleTextPacket build() {
        ChatMessage message = getText() == null ? null : new ChatMessage(getText());
        return new ClientboundSetTitleTextPacket(message);
    }
}

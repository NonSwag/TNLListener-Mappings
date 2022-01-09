package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.nonswag.tnl.listener.api.packets.TitlePacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class NMSTitlePacket extends TitlePacket {

    NMSTitlePacket(@Nonnull Action action, @Nullable String text, int timeIn, int timeStay, int timeOut) {
        super(action, text, timeIn, timeStay, timeOut);
    }

    @Nonnull
    @Override
    public PacketPlayOutTitle build() {
        ChatMessage message = getText() == null ? null : new ChatMessage(getText());
        return new PacketPlayOutTitle(action(), message, getTimeIn(), getTimeStay(), getTimeOut());
    }

    @Nonnull
    private PacketPlayOutTitle.EnumTitleAction action() {
        return switch (getAction()) {
            case CLEAR -> PacketPlayOutTitle.EnumTitleAction.CLEAR;
            case TITLE -> PacketPlayOutTitle.EnumTitleAction.TITLE;
            case RESET -> PacketPlayOutTitle.EnumTitleAction.RESET;
            case TIMES -> PacketPlayOutTitle.EnumTitleAction.TIMES;
            case SUBTITLE -> PacketPlayOutTitle.EnumTitleAction.SUBTITLE;
            case ACTIONBAR -> PacketPlayOutTitle.EnumTitleAction.ACTIONBAR;
        };
    }
}

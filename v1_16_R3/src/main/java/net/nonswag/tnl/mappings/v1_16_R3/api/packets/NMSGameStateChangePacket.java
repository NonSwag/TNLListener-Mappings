package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
import net.nonswag.tnl.listener.api.packets.GameStateChangePacket;

import javax.annotation.Nonnull;

public final class NMSGameStateChangePacket extends GameStateChangePacket {

    NMSGameStateChangePacket(@Nonnull Identifier identifier, float state) {
        super(identifier, state);
    }

    @Nonnull
    @Override
    public PacketPlayOutGameStateChange build() {
        return new PacketPlayOutGameStateChange(identifier(), getState());
    }

    @Nonnull
    private PacketPlayOutGameStateChange.a identifier() {
        return switch (getIdentifier().getId()) {
            case 0 -> PacketPlayOutGameStateChange.a;
            case 1 -> PacketPlayOutGameStateChange.b;
            case 2 -> PacketPlayOutGameStateChange.c;
            case 3 -> PacketPlayOutGameStateChange.d;
            case 4 -> PacketPlayOutGameStateChange.e;
            case 5 -> PacketPlayOutGameStateChange.f;
            case 6 -> PacketPlayOutGameStateChange.g;
            case 7 -> PacketPlayOutGameStateChange.h;
            case 8 -> PacketPlayOutGameStateChange.i;
            case 9 -> PacketPlayOutGameStateChange.j;
            case 10 -> PacketPlayOutGameStateChange.k;
            case 11 -> PacketPlayOutGameStateChange.l;
            default -> throw new IllegalStateException("Unexpected value: " + getIdentifier().getId());
        };
    }
}

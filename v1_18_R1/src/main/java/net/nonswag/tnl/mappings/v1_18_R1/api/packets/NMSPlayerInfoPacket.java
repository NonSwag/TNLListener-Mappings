package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.nonswag.tnl.listener.api.packets.PlayerInfoPacket;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public final class NMSPlayerInfoPacket extends PlayerInfoPacket {

    NMSPlayerInfoPacket(@Nonnull Player player, @Nonnull Action action) {
        super(player, action);
    }

    @Nonnull
    @Override
    public PacketPlayOutPlayerInfo build() {
        return new PacketPlayOutPlayerInfo(action(), ((CraftPlayer) getPlayer()).getHandle());
    }

    @Nonnull
    private PacketPlayOutPlayerInfo.EnumPlayerInfoAction action() {
        return switch (getAction()) {
            case ADD_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a;
            case UPDATE_GAME_MODE -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b;
            case UPDATE_LATENCY -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.c;
            case UPDATE_DISPLAY_NAME -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.d;
            case REMOVE_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e;
        };
    }
}

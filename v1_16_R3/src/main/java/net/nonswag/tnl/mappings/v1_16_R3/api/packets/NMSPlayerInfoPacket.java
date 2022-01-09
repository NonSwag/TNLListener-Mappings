package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.nonswag.tnl.listener.api.packets.PlayerInfoPacket;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
            case ADD_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER;
            case REMOVE_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER;
            case UPDATE_LATENCY -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY;
            case UPDATE_GAME_MODE -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE;
            case UPDATE_DISPLAY_NAME -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME;
        };
    }
}

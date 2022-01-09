package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutBoss;
import net.nonswag.tnl.listener.api.packets.BossBarPacket;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftBossBar;

import javax.annotation.Nonnull;

public final class NMSBossBarPacket extends BossBarPacket {

    NMSBossBarPacket(@Nonnull Action action, @Nonnull BossBar bossBar) {
        super(action, bossBar);
    }

    @Nonnull
    @Override
    public PacketPlayOutBoss build() {
        return new PacketPlayOutBoss(action(), ((CraftBossBar) getBossBar()).getHandle());
    }

    @Nonnull
    private PacketPlayOutBoss.Action action() {
        return switch (getAction()) {
            case ADD -> PacketPlayOutBoss.Action.ADD;
            case REMOVE -> PacketPlayOutBoss.Action.REMOVE;
            case UPDATE_PCT -> PacketPlayOutBoss.Action.UPDATE_PCT;
            case UPDATE_NAME -> PacketPlayOutBoss.Action.UPDATE_NAME;
            case UPDATE_STYLE -> PacketPlayOutBoss.Action.UPDATE_STYLE;
            case UPDATE_PROPERTIES -> PacketPlayOutBoss.Action.UPDATE_PROPERTIES;
        };
    }
}

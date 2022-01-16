package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutBoss;
import net.nonswag.tnl.listener.api.packets.BossBarPacket;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R1.boss.CraftBossBar;

import javax.annotation.Nonnull;

public final class NMSBossBarPacket extends BossBarPacket {

    NMSBossBarPacket(@Nonnull Action action, @Nonnull BossBar bossBar) {
        super(action, bossBar);
    }

    @Nonnull
    @Override
    public PacketPlayOutBoss build() {
        return PacketPlayOutBoss.a(((CraftBossBar) getBossBar()).getHandle());
    }
}

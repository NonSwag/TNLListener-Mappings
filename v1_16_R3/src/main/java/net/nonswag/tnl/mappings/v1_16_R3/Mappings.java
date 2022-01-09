package net.nonswag.tnl.mappings.v1_16_R3;

import net.nonswag.tnl.listener.api.bossbar.TNLBossBar;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.mapper.Mapping;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.version.Version;
import net.nonswag.tnl.mappings.v1_16_R3.api.bossbar.NMSBossBar;
import net.nonswag.tnl.mappings.v1_16_R3.api.item.NMSItem;
import net.nonswag.tnl.mappings.v1_16_R3.api.player.NMSPlayer;
import net.nonswag.tnl.mappings.v1_16_R3.listeners.PacketListener;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@Mapping.Info(name = "Origin 1.16.5", authors = "NonSwag", description = "An official TNL-Production")
public class Mappings extends Mapping {

    @Override
    public void enable() {
        getEventManager().registerListener(new PacketListener());
    }

    @Nonnull
    @Override
    public Version getVersion() {
        return Version.v1_16_4;
    }

    @Nonnull
    @Override
    public TNLPlayer createPlayer(@Nonnull Player player) {
        return new NMSPlayer(player);
    }

    @Nonnull
    @Override
    public TNLItem createItem(@Nonnull ItemStack itemStack) {
        return new NMSItem(itemStack);
    }

    @Nonnull
    @Override
    public TNLBossBar createBossBar(@Nonnull String id, @Nonnull String text, @Nonnull BarColor color, @Nonnull BarStyle style, double progress, @Nonnull BarFlag... flags) {
        return new NMSBossBar(id, text, color, style, progress, flags);
    }
}

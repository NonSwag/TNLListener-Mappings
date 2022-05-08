package net.nonswag.tnl.mappings.v1_16_R3.api.plugin;

import net.nonswag.tnl.listener.api.plugin.PluginHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;

import javax.annotation.Nonnull;

public class NMSPluginHelper extends PluginHelper {

    @Nonnull
    private static CraftServer getCraftServer() {
        return (CraftServer) Bukkit.getServer();
    }

    @Nonnull
    @Override
    public SimpleCommandMap getCommandMap() {
        return getCraftServer().getServer().server.getCommandMap();
    }
}

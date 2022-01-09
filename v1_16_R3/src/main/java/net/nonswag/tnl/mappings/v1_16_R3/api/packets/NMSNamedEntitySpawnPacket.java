package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.nonswag.tnl.listener.api.packets.NamedEntitySpawnPacket;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import javax.annotation.Nonnull;

public final class NMSNamedEntitySpawnPacket extends NamedEntitySpawnPacket {

    NMSNamedEntitySpawnPacket(@Nonnull HumanEntity player) {
        super(player);
    }

    @Nonnull
    @Override
    public PacketPlayOutNamedEntitySpawn build() {
        return new PacketPlayOutNamedEntitySpawn(((CraftHumanEntity) getPlayer()).getHandle());
    }
}

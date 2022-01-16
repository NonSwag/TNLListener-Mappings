package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.nonswag.tnl.listener.api.packets.EntitySpawnPacket;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public final class NMSEntitySpawnPacket extends EntitySpawnPacket {

    NMSEntitySpawnPacket(@Nonnull Entity entity) {
        super(entity);
    }

    @Nonnull
    @Override
    public PacketPlayOutSpawnEntity build() {
        return new PacketPlayOutSpawnEntity(((CraftEntity) getEntity()).getHandle());
    }
}

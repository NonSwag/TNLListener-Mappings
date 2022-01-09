package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import net.nonswag.tnl.listener.api.packets.EntitySpawnPacket;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
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

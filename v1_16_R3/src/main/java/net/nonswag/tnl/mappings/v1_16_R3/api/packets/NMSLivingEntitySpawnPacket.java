package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.nonswag.tnl.listener.api.packets.LivingEntitySpawnPacket;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;

public final class NMSLivingEntitySpawnPacket extends LivingEntitySpawnPacket {

    NMSLivingEntitySpawnPacket(@Nonnull LivingEntity entity) {
        super(entity);
    }

    @Nonnull
    @Override
    public PacketPlayOutSpawnEntityLiving build() {
        return new PacketPlayOutSpawnEntityLiving(((CraftLivingEntity) getEntity()).getHandle());
    }
}

package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutSetCooldown;
import net.nonswag.tnl.listener.api.packets.CooldownPacket;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

import javax.annotation.Nonnull;

public final class NMSCooldownPacket extends CooldownPacket {

    NMSCooldownPacket(@Nonnull Material item, int cooldown) {
        super(item, cooldown);
    }

    @Nonnull
    @Override
    public PacketPlayOutSetCooldown build() {
        return new PacketPlayOutSetCooldown(CraftMagicNumbers.getItem(getItem()), getCooldown());
    }
}

package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.packets.SetSlotPacket;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class NMSSetSlotPacket extends SetSlotPacket {

    NMSSetSlotPacket(@Nonnull Inventory inventory, int slot, @Nullable ItemStack itemStack) {
        super(inventory, slot, itemStack);
    }

    @Nonnull
    @Override
    public PacketPlayOutSetSlot build() {
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot();
        Reflection.setField(packet, "a", getInventory().getId());
        Reflection.setField(packet, "b", getSlot());
        if (getItemStack() != null) Reflection.setField(packet, "c", CraftItemStack.asNMSCopy(getItemStack()));
        return packet;
    }
}

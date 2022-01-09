package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.NonNullList;
import net.minecraft.server.v1_16_R3.PacketPlayOutWindowItems;
import net.nonswag.tnl.listener.api.packets.WindowItemsPacket;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public final class NMSWindowItemsPacket extends WindowItemsPacket {

    NMSWindowItemsPacket(int windowId, @Nonnull List<ItemStack> items) {
        super(windowId, items);
    }

    @Nonnull
    @Override
    public PacketPlayOutWindowItems build() {
        NonNullList<net.minecraft.server.v1_16_R3.ItemStack> items = NonNullList.a();
        for (ItemStack item : getItems()) items.add(CraftItemStack.asNMSCopy(item));
        return new PacketPlayOutWindowItems(getWindowId(), items);
    }
}

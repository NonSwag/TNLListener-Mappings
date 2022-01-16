package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.world.item.Items;
import net.nonswag.tnl.listener.api.packets.WindowItemsPacket;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
        NonNullList<net.minecraft.world.item.ItemStack> items = NonNullList.a();
        for (ItemStack item : getItems()) items.add(CraftItemStack.asNMSCopy(item));
        return new PacketPlayOutWindowItems(getWindowId(), getWindowId(), items, Items.a.O_());
    }
}

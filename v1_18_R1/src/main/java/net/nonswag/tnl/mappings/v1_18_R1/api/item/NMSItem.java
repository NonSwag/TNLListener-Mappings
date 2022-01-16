package net.nonswag.tnl.mappings.v1_18_R1.api.item;

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.item.TNLItem;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class NMSItem extends TNLItem {

    public NMSItem(@Nonnull ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public int getMaxDurability() {
        return CraftItemStack.asNMSCopy(this).c().m();
    }

    @Override
    public int getCustomModelData() {
        return getNBT().h("{CustomModelData}");
    }

    @Nonnull
    @Override
    public NMSItem modifyNBT(@Nonnull String nbt) {
        try {
            net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(this);
            item.b(MojangsonParser.a(nbt));
            setItemMeta(CraftItemStack.getItemMeta(item));
        } catch (Exception e) {
            Logger.error.println("Failed to modify item nbt ", e);
        }
        return this;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(this).t();
    }

    @Nonnull
    @Override
    public NMSItem duplicate() {
        return new NMSItem(this);
    }
}

package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.item.TNLItem;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class NMSItem extends TNLItem {

    public NMSItem(@Nonnull ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public int getMaxDurability() {
        return CraftItemStack.asNMSCopy(this).getItem().getMaxDurability();
    }

    @Override
    public int getCustomModelData() {
        return getNBT().getInt("{CustomModelData}");
    }

    @Nonnull
    @Override
    public NMSItem modifyNBT(@Nonnull String nbt) {
        net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(this);
        try {
            item.setTag(item.getOrCreateTag().a(MojangsonParser.parse(nbt)));
        } catch (CommandSyntaxException e) {
            Logger.error.println("Failed to modify item nbt", e);
        }
        setItemMeta(CraftItemStack.getItemMeta(item));
        return this;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBT() {
        return CraftItemStack.asNMSCopy(this).getOrCreateTag();
    }

    @Nonnull
    @Override
    public NMSItem duplicate() {
        return new NMSItem(this);
    }
}

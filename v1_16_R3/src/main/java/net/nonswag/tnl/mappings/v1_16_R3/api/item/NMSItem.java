package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.nbt.NBTTag;
import net.nonswag.tnl.mappings.v1_16_R3.api.nbt.NBT;
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

    @Nonnull
    @Override
    public NMSItem modifyNBT(@Nonnull String nbt) {
        try {
            setNBT(getNBT().append(new NBT(MojangsonParser.parse(nbt))));
        } catch (CommandSyntaxException e) {
            Logger.error.println("Failed to modify item nbt", e);
        }
        return this;
    }

    @Nonnull
    @Override
    public NBT getNBT() {
        return new NBT(CraftItemStack.asNMSCopy(this).getOrCreateTag());
    }

    @Nonnull
    @Override
    public NMSItem setNBT(@Nonnull NBTTag nbt) {
        net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(this);
        item.setTag(nbt.versioned());
        setItemMeta(CraftItemStack.getItemMeta(item));
        return this;
    }

    @Nonnull
    @Override
    public NMSItem duplicate() {
        return new NMSItem(this);
    }
}

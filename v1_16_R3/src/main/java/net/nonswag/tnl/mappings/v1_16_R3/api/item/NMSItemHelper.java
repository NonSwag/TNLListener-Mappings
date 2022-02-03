package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import net.minecraft.server.v1_16_R3.Item;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.item.ItemHelper;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class NMSItemHelper extends ItemHelper {

    @Override
    public void setMaxStackSize(@Nonnull Material material, int maxStackSize) {
        Reflection.setField(material, "maxStack", maxStackSize);
        Reflection.setField(Item.getById(material.ordinal()), Item.class, "maxStackSize", maxStackSize);
    }

    @Override
    public void setDurability(@Nonnull Material material, int durability) {
        Reflection.setField(material, "durability", durability);
        Reflection.setField(Item.getById(material.ordinal()), Item.class, "durability", durability);
    }
}

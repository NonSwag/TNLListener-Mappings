package net.nonswag.tnl.mappings.v1_18_R1.api.item;

import net.minecraft.world.item.Item;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.item.ItemHelper;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class NMSItemHelper extends ItemHelper {

    @Override
    public void setMaxStackSize(@Nonnull Material material, int maxStackSize) {
        Reflection.setField(material, "maxStack", maxStackSize);
        Reflection.setField(Item.b(material.ordinal()), "maxStackSize", maxStackSize);
    }

    @Override
    public void setDurability(@Nonnull Material material, int durability) {
        Reflection.setField(material, "durability", durability);
        Reflection.setField(Item.b(material.ordinal()), "durability", durability);
    }
}

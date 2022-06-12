package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import lombok.Getter;
import net.minecraft.server.v1_16_R3.BlockComposter;
import net.minecraft.server.v1_16_R3.IMaterial;
import net.minecraft.server.v1_16_R3.Item;
import net.nonswag.tnl.core.api.object.IntelligentMap;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.item.ItemHelper;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class NMSItemHelper extends ItemHelper {

    @Nullable
    private HashMap<Material, Float> compostableItems = null;

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

    @Override
    public void resetCompostableItems() {
        getCompostableItems().clear();
        compostableItems = null;
        BlockComposter.c();
    }

    @Nonnull
    @Override
    public HashMap<Material, Float> getCompostableItems() {
        if (compostableItems != null) return compostableItems;
        HashMap<Material, Float> items = new HashMap<>();
        Map<IMaterial, Float> map = Reflection.<Map<IMaterial, Float>>getStaticField(BlockComposter.class, "b").getOrDefault(new HashMap<>());
        map.forEach((item, weight) -> items.put(CraftMagicNumbers.getMaterial(item.getItem()), weight));
        return compostableItems = new IntelligentMap<>(items) {
            @Override
            public void update() {
                map.clear();
                forEach((item, weight) -> map.put(CraftMagicNumbers.getItem(item), weight));
            }
        };
    }
}

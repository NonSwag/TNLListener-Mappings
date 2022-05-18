package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.MobEffect;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.item.FoodProperties;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.nbt.NBTTag;
import net.nonswag.tnl.mappings.v1_16_R3.api.nbt.NBT;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

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

    @Nullable
    @Override
    public FoodProperties getFoodProperties() {
        net.minecraft.server.v1_16_R3.FoodInfo info = CraftItemStack.asNMSCopy(this).getItem().getFoodInfo();
        if (info == null) return null;
        HashMap<PotionEffect, Float> effects = new HashMap<>();
        for (Pair<MobEffect, Float> pair : info.f()) {
            MobEffect first = pair.getFirst();
            CraftPotionEffectType type = new CraftPotionEffectType(first.getMobEffect());
            effects.put(new PotionEffect(type, first.getDuration(), first.getAmplifier(), first.isAmbient(), first.isShowParticles(), first.isShowIcon()), pair.getSecond());
        }
        return new FoodProperties(info.getNutrition(), info.getSaturationModifier(), info.c(), info.d(), info.e(), effects);
    }

    @Override
    public boolean isFood() {
        return CraftItemStack.asNMSCopy(this).getItem().isFood();
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

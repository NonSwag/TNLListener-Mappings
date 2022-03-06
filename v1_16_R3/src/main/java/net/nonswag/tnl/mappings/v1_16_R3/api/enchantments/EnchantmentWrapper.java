package net.nonswag.tnl.mappings.v1_16_R3.api.enchantments;

import lombok.Getter;
import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.listener.api.enchantment.Enchant;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentTarget;

import javax.annotation.Nonnull;

@Getter
public class EnchantmentWrapper extends Enchant {

    @Nonnull
    private final Enchantment vanilla;
    @Nonnull
    private final MinecraftKey namespace;

    public EnchantmentWrapper(@Nonnull NamespacedKey key, @Nonnull String name, @Nonnull EnchantmentTarget target) {
        super(key, name, target);
        this.namespace = new MinecraftKey(getKey().getNamespace(), getKey().getKey());
        this.vanilla = new Enchantment(Enchantment.Rarity.COMMON, EnchantmentSlotType.ARMOR, EnumItemSlot.values()) {
            @Override
            public int getMaxLevel() {
                return EnchantmentWrapper.this.getMaxLevel();
            }

            @Override
            public int getStartLevel() {
                return EnchantmentWrapper.this.getStartLevel();
            }

            @Override
            public boolean isTreasure() {
                return EnchantmentWrapper.this.isTreasure();
            }

            @Override
            public boolean c() {
                return EnchantmentWrapper.this.isCursed();
            }

            @Override
            protected String f() {
                return EnchantmentWrapper.this.getName();
            }

            @Override
            public String g() {
                return this.f();
            }

            @Override
            public boolean canEnchant(@Nonnull ItemStack itemStack) {
                return EnchantmentWrapper.this.canEnchantItem(CraftItemStack.asBukkitCopy(itemStack));
            }

            @Override
            public Rarity d() {
                return switch (getRarity()) {
                    case COMMON -> Rarity.COMMON;
                    case UNCOMMON -> Rarity.UNCOMMON;
                    case RARE -> Rarity.RARE;
                    case VERY_RARE -> Rarity.VERY_RARE;
                };
            }
        };
        this.register();
    }

    @Nonnull
    @Override
    protected Enchant register() {
        IRegistry.a(IRegistry.ENCHANTMENT, namespace, vanilla);
        return super.register();
    }
}

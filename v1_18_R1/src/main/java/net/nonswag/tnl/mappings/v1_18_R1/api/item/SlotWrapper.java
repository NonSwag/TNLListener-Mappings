package net.nonswag.tnl.mappings.v1_18_R1.api.item;

import net.minecraft.world.entity.EnumItemSlot;
import net.nonswag.tnl.listener.api.item.SlotType;

import javax.annotation.Nonnull;

public interface SlotWrapper {

    @Nonnull
    default EnumItemSlot wrap(@Nonnull SlotType type) {
        return switch (type) {
            case MAIN_HAND -> EnumItemSlot.a;
            case OFF_HAND -> EnumItemSlot.b;
            case HELMET -> EnumItemSlot.f;
            case CHESTPLATE -> EnumItemSlot.e;
            case LEGGINGS -> EnumItemSlot.d;
            case BOOTS -> EnumItemSlot.c;
        };
    }
}

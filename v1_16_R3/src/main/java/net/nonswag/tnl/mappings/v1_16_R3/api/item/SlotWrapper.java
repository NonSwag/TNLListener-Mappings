package net.nonswag.tnl.mappings.v1_16_R3.api.item;

import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.nonswag.tnl.listener.api.item.SlotType;

import javax.annotation.Nonnull;

public interface SlotWrapper {

    @Nonnull
    default EnumItemSlot wrap(@Nonnull SlotType type) {
        return switch (type) {
            case MAIN_HAND -> EnumItemSlot.MAINHAND;
            case OFF_HAND -> EnumItemSlot.OFFHAND;
            case HELMET -> EnumItemSlot.HEAD;
            case CHESTPLATE -> EnumItemSlot.CHEST;
            case LEGGINGS -> EnumItemSlot.LEGS;
            case BOOTS -> EnumItemSlot.FEET;
        };
    }
}

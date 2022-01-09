package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R3.WorldBorder;
import net.nonswag.tnl.listener.api.border.VirtualBorder;
import net.nonswag.tnl.listener.api.packets.WorldBorderPacket;

import javax.annotation.Nonnull;

public final class NMSWorldBorderPacket extends WorldBorderPacket {

    NMSWorldBorderPacket(@Nonnull VirtualBorder virtualBorder, @Nonnull Action action) {
        super(virtualBorder, action);
    }

    @Nonnull
    @Override
    public PacketPlayOutWorldBorder build() {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.world = ((org.bukkit.craftbukkit.v1_16_R3.CraftWorld) getBorder().getWorld()).getHandle();
        worldBorder.setWarningDistance(getBorder().getWarningDistance());
        worldBorder.setSize(getBorder().getSize());
        worldBorder.setCenter(getBorder().getCenter().x(), getBorder().getCenter().z());
        worldBorder.setDamageAmount(getBorder().getDamageAmount());
        worldBorder.setDamageBuffer(getBorder().getDamageBuffer());
        worldBorder.setWarningTime(getBorder().getWarningTime());
        return new PacketPlayOutWorldBorder(worldBorder, action());
    }

    @Nonnull
    private PacketPlayOutWorldBorder.EnumWorldBorderAction action() {
        return switch (getAction()) {
            case SET_SIZE -> PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE;
            case INITIALIZE -> PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE;
            case LERP_SIZE -> PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE;
            case SET_CENTER -> PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER;
            case SET_WARNING_BLOCKS -> PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS;
            case SET_WARNING_TIME -> PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME;
        };
    }
}

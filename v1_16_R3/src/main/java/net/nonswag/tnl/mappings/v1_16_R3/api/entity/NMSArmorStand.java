package net.nonswag.tnl.mappings.v1_16_R3.api.entity;

import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.mappings.v1_16_R3.api.item.SlotWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NMSArmorStand extends EntityArmorStand implements TNLArmorStand, SlotWrapper {

    public NMSArmorStand(@Nonnull World world, double x, double y, double z, float yaw, float pitch) {
        super(((CraftWorld) world).getHandle(), x, y, z);
        super.setYawPitch(yaw, pitch);
    }

    @Override
    public void setX(double x) {
        super.f(x, super.locY(), super.locZ());
    }

    @Override
    public void setY(double y) {
        super.f(super.locX(), y, super.locZ());
    }

    @Override
    public void setZ(double z) {
        super.f(super.locX(), super.locY(), z);
    }

    @Override
    public void setHeadPose(@Nullable Pose pose) {
        if (pose != null) super.setHeadPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setBodyPose(@Nullable Pose pose) {
        if (pose != null) super.setBodyPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftArmPose(@Nullable Pose pose) {
        if (pose != null) super.setLeftArmPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightArmPose(@Nullable Pose pose) {
        if (pose != null) super.setRightArmPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftLegPose(@Nullable Pose pose) {
        if (pose != null) super.setLeftLegPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightLegPose(@Nullable Pose pose) {
        if (pose != null) super.setRightLegPose(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setCustomName(@Nonnull String customName) {
        IChatBaseComponent[] components = CraftChatMessage.fromString(customName);
        super.setCustomName(components[0]);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setInvisible(!visible);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setNoGravity(!gravity);
    }

    @Override
    public void setBasePlate(boolean flag) {
        super.setBasePlate(!flag);
    }

    @Override
    public void setItemInMainHand(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setItemInOffHand(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setHelmet(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setChestplate(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setLeggings(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setBoots(@Nullable TNLItem item) {
        super.setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public DataWatcher getDataWatcher() {
        return super.getDataWatcher();
    }

    @Override
    public void setLocation(@Nonnull Location location) {
        setLocation(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void setLocation(double x, double y, double z) {
        setPosition(x, y, z);
    }

    @Override
    public void setItem(@Nonnull SlotType slot, @Nonnull TNLItem item) {
        setSlot(wrap(slot), CraftItemStack.asNMSCopy(item.getItemStack()), true);
    }

    @Override
    public int getEntityId() {
        return super.getId();
    }

    @Nonnull
    @Override
    public CraftLivingEntity bukkit() {
        return (CraftLivingEntity) super.getBukkitEntity();
    }
}

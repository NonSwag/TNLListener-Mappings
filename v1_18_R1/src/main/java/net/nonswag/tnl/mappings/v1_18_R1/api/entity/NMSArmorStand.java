package net.nonswag.tnl.mappings.v1_18_R1.api.entity;

import net.minecraft.core.Vector3f;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.nonswag.tnl.listener.api.entity.TNLArmorStand;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.mappings.v1_18_R1.api.item.SlotWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.bukkit.inventory.EntityEquipment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NMSArmorStand extends CraftArmorStand implements TNLArmorStand, SlotWrapper {

    public NMSArmorStand(@Nonnull World world, double x, double y, double z, float yaw, float pitch) {
        super((CraftServer) Bukkit.getServer(), new EntityArmorStand(((CraftWorld) world).getHandle(), x, y, z));
        getLocation().setYaw(yaw);
        getLocation().setYaw(pitch);
    }

    @Override
    public void setX(double x) {
        getLocation().setX(x);
    }

    @Override
    public void setY(double y) {
        getLocation().setY(y);
    }

    @Override
    public void setZ(double z) {
        getLocation().setZ(z);
    }

    @Override
    public void updateSize() {

    }

    @Override
    public boolean doAITick() {
        return false;
    }

    @Override
    public void setHeadRotation(float v) {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void killEntity() {

    }

    @Override
    public void setHeadPose(@Nullable Pose pose) {
        if (pose != null) getHandle().a(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setBodyPose(@Nullable Pose pose) {
        if (pose != null) getHandle().b(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftArmPose(@Nullable Pose pose) {
        if (pose != null) getHandle().d(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightArmPose(@Nullable Pose pose) {
        if (pose != null) getHandle().d(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setLeftLegPose(@Nullable Pose pose) {
        if (pose != null) getHandle().e(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public void setRightLegPose(@Nullable Pose pose) {
        if (pose != null) getHandle().f(new Vector3f(pose.getX(), pose.getY(), pose.getZ()));
    }

    @Override
    public boolean isInteractable() {
        return !isDead() && isCollidable() && !isMarker();
    }

    @Override
    public void setCustomName(@Nonnull String customName) {
        IChatBaseComponent[] components = CraftChatMessage.fromString(customName);
        getHandle().a(components[0]);
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().j(!visible);
    }

    @Override
    public void setInvulnerable(boolean b) {

    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().Q = !gravity;
    }

    @Override
    public void setBasePlate(boolean flag) {
        getHandle().s(!flag);
    }

    @Override
    public void setItemInMainHand(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.a, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setItemInOffHand(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.b, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setHelmet(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setChestplate(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.e, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setLeggings(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.d, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public void setBoots(@Nullable TNLItem item) {
        getHandle().setItemSlot(EnumItemSlot.c, CraftItemStack.asNMSCopy(item != null ? item.getItemStack() : null), true);
    }

    @Override
    public DataWatcher getDataWatcher() {
        return getHandle().ai();
    }

    @Override
    public void setLocation(@Nonnull Location location) {
        setLocation(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void setLocation(double x, double y, double z) {
        getLocation().setX(x);
        getLocation().setY(y);
        getLocation().setZ(z);
    }

    @Override
    public void setLocation(double x, double y, double z, float yaw, float pitch) {
        setLocation(x, y, z);
        getLocation().setYaw(yaw);
        getLocation().setPitch(pitch);
    }

    @Override
    public void setItem(@Nonnull SlotType slot, @Nonnull TNLItem item) {
        EntityEquipment equipment = bukkit().getEquipment();
        if (equipment != null) equipment.setItem(slot.bukkit(), item);
    }

    @Override
    public int getEntityId() {
        return bukkit().getEntityId();
    }

    @Nonnull
    @Override
    public CraftArmorStand bukkit() {
        return this;
    }
}

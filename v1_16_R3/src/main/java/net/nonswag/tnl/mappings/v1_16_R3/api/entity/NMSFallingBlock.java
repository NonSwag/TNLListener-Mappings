package net.nonswag.tnl.mappings.v1_16_R3.api.entity;

import net.minecraft.server.v1_16_R3.EntityFallingBlock;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.nonswag.tnl.core.api.object.Objects;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.entity.TNLFallingBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public class NMSFallingBlock extends EntityFallingBlock implements TNLFallingBlock {

    public NMSFallingBlock(@Nonnull Location location, @Nonnull Material type) {
        super(((CraftWorld) Objects.nonnull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ(), ((CraftBlockData) type.createBlockData()).getState());
        ticksLived = 1;
        persist = true;
        setNoGravity(true);
    }

    @Nonnull
    @Override
    public NMSFallingBlock setType(@Nonnull Material type) {
        Reflection.setField(this, "block", ((CraftBlockData) type.createBlockData()).getState());
        return this;
    }

    @Nonnull
    @Override
    public NMSFallingBlock setGlowing(boolean glowing) {
        this.glowing = glowing;
        setFlag(6, true);
        return this;
    }

    @Nonnull
    @Override
    public NMSFallingBlock teleport(@Nonnull Location location) {
        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        return this;
    }

    @Nonnull
    @Override
    public NMSFallingBlock teleport(@Nonnull Entity entity) {
        teleport(entity.getLocation());
        return this;
    }

    @Nonnull
    @Override
    public NMSFallingBlock teleport(@Nonnull TNLEntity entity) {
        teleport(entity.bukkit());
        return this;
    }

    @Nonnull
    @Override
    public NMSFallingBlock setCustomName(@Nonnull String customName) {
        IChatBaseComponent[] components = CraftChatMessage.fromString(customName);
        super.setCustomName(components[0]);
        return this;
    }

    @Override
    public int getEntityId() {
        return super.getId();
    }

    @Nonnull
    @Override
    public CraftEntity bukkit() {
        return super.getBukkitEntity();
    }
}

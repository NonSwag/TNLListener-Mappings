package net.nonswag.tnl.mappings.v1_18_R1.api.entity;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.entity.TNLFallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class NMSFallingBlock extends CraftFallingBlock implements TNLFallingBlock {

    public NMSFallingBlock(@Nonnull Location location, @Nonnull Material type) {
        super((CraftServer) Bukkit.getServer(), new EntityFallingBlock(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(),
                        location.getX(), location.getY(), location.getZ(), ((CraftBlockData) type.createBlockData()).getState()));
        getHandle().S = 1;
        getHandle().persist = true;
        getHandle().e(true);
    }

    @Override
    public void setType(@Nonnull Material type) {
        Reflection.setField(getHandle(), "ap", ((CraftBlockData) type.createBlockData()).getState());
    }

    @Override
    public boolean teleport(@Nonnull Location location) {
        return super.teleport(location);
    }

    @Override
    public boolean teleport(@Nonnull Entity destination) {
        return super.teleport(destination);
    }

    @Override
    public boolean teleport(@Nonnull TNLEntity entity) {
        return teleport(entity.bukkit());
    }

    @Override
    public void setCustomName(@Nonnull String customName) {
        IChatBaseComponent[] components = CraftChatMessage.fromString(customName);
        getHandle().a(components[0]);
    }

    @Override
    public int getEntityId() {
        return getHandle().ae();
    }

    @Nonnull
    @Override
    public CraftFallingBlock bukkit() {
        return this;
    }
}

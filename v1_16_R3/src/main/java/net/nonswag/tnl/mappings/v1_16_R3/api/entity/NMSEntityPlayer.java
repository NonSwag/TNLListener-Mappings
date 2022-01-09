package net.nonswag.tnl.mappings.v1_16_R3.api.entity;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.GameProfile;
import net.nonswag.tnl.listener.api.player.Skin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import javax.annotation.Nonnull;

public class NMSEntityPlayer extends EntityPlayer implements TNLEntityPlayer {

    @Nonnull
    private final GameProfile gameProfile;
    private boolean cape = true;

    public NMSEntityPlayer(@Nonnull World world, double x, double y, double z, float yaw, float pitch, @Nonnull GameProfile profile) {
        super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) world).getHandle(),
                new com.mojang.authlib.GameProfile(profile.getUniqueId(), profile.getName()),
                new PlayerInteractManager(((CraftWorld) world).getHandle()));
        super.setLocation(x, y, z, yaw, pitch);
        super.setHeadRotation(yaw);
        Skin skin = profile.getSkin();
        if (skin != null) {
            super.getProfile().getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }
        super.getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 126);
        this.gameProfile = profile;
    }

    @Override
    public void setItem(@Nonnull SlotType slot, @Nonnull TNLItem item) {
        setSlot(slot.nms(), CraftItemStack.asNMSCopy(item.getItemStack()), true);
    }

    @Override
    public void setPing(int ping) {
        super.ping = ping;
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    @Override
    public int getPing() {
        return super.ping;
    }

    @Override
    @Nonnull
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public void setCapeVisibility(boolean visible) {
        this.cape = visible;
        super.getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) (cape ? 127 : 126));
    }

    @Override
    public boolean getCapeVisibility() {
        return cape;
    }

    @Override
    public void setLocation(@Nonnull Location location) {
        super.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void setLocation(double x, double y, double z) {
        super.setLocation(x, y, z, yaw, pitch);
    }

    @Override
    public int getEntityId() {
        return super.getId();
    }

    @Nonnull
    @Override
    public CraftPlayer bukkit() {
        return super.getBukkitEntity();
    }
}

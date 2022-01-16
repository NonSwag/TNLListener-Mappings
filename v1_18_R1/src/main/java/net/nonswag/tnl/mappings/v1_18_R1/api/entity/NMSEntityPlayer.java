package net.nonswag.tnl.mappings.v1_18_R1.api.entity;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.GameProfile;
import net.nonswag.tnl.listener.api.player.Skin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.inventory.EntityEquipment;

import javax.annotation.Nonnull;

public class NMSEntityPlayer extends CraftPlayer implements TNLEntityPlayer {

    @Nonnull
    private final GameProfile gameProfile;
    private boolean cape = true;

    public NMSEntityPlayer(@Nonnull World world, double x, double y, double z, float yaw, float pitch, @Nonnull GameProfile profile) {
        super((CraftServer) Bukkit.getServer(), new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) world).getHandle(), new com.mojang.authlib.GameProfile(profile.getUniqueId(), profile.getName())));
        setLocation(x, y, z, yaw, pitch);
        Skin skin = profile.getSkin();
        if (skin != null) {
            Multimap<String, Property> delegate = Reflection.getField(getHandle().fp().getProperties(), Multimap.class, "properties").nonnull();
            delegate.put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }
        getHandle().ai().a(DataWatcherRegistry.a.a(16), (byte) 126);
        this.gameProfile = profile;
    }

    @Override
    public void setItem(@Nonnull SlotType slot, @Nonnull TNLItem item) {
        EntityEquipment equipment = bukkit().getEquipment();
        if (equipment != null) equipment.setItem(slot.bukkit(), item);
    }

    @Override
    public void setPing(int ping) {
        getHandle().e = ping;
    }

    @Override
    @Nonnull
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public void setCapeVisibility(boolean visible) {
        this.cape = visible;
        getHandle().ai().a(DataWatcherRegistry.a.a(16), (byte) (cape ? 127 : 126));
    }

    @Override
    public boolean getCapeVisibility() {
        return cape;
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
    public int getEntityId() {
        return getHandle().ae();
    }

    @Nonnull
    @Override
    public CraftPlayer bukkit() {
        return this;
    }
}

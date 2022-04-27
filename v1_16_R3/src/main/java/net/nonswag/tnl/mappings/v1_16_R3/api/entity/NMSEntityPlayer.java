package net.nonswag.tnl.mappings.v1_16_R3.api.entity;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityPose;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.GameProfile;
import net.nonswag.tnl.listener.api.player.Skin;
import net.nonswag.tnl.mappings.v1_16_R3.api.item.SlotWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import javax.annotation.Nonnull;

public class NMSEntityPlayer extends EntityPlayer implements TNLEntityPlayer, SlotWrapper {

    @Nonnull
    private final GameProfile gameProfile;
    private boolean cape = false;

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
        setCapeVisibility(false);
        this.gameProfile = profile;
    }

    @Override
    public void setItem(@Nonnull SlotType slot, @Nonnull TNLItem item) {
        setSlot(wrap(slot), CraftItemStack.asNMSCopy(item.getItemStack()), true);
    }

    @Override
    public void setPing(int ping) {
        super.ping = ping;
    }

    @Override
    public void setGlowing(boolean glowing) {
        setFlag(6, glowing);
        super.glowing = glowing;
    }

    @Override
    public int getPing() {
        return super.ping;
    }

    @Override
    public void setPlayerPose(@Nonnull Pose pose) {
        this.datawatcher.set(POSE, switch (pose) {
            case SNEAKING -> EntityPose.CROUCHING;
            case DYING -> EntityPose.DYING;
            case FALL_FLYING -> EntityPose.FALL_FLYING;
            case SLEEPING -> EntityPose.SLEEPING;
            case SPIN_ATTACK -> EntityPose.SPIN_ATTACK;
            case STANDING -> EntityPose.STANDING;
            case SWIMMING -> EntityPose.SWIMMING;
        });
    }

    @Nonnull
    @Override
    public Pose getPlayerPose() {
        return switch (getPose()) {
            case CROUCHING -> Pose.SNEAKING;
            case DYING -> Pose.DYING;
            case FALL_FLYING -> Pose.FALL_FLYING;
            case SLEEPING -> Pose.SLEEPING;
            case SPIN_ATTACK -> Pose.SPIN_ATTACK;
            case STANDING -> Pose.STANDING;
            case SWIMMING -> Pose.SWIMMING;
        };
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

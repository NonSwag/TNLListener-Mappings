package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.nonswag.tnl.listener.api.border.VirtualBorder;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.location.BlockLocation;
import net.nonswag.tnl.listener.api.location.Position;
import net.nonswag.tnl.listener.api.mapper.Mapping;
import net.nonswag.tnl.listener.api.packets.BlockBreakAnimationPacket;
import net.nonswag.tnl.listener.api.packets.BossBarPacket;
import net.nonswag.tnl.listener.api.packets.CameraPacket;
import net.nonswag.tnl.listener.api.packets.ChatPacket;
import net.nonswag.tnl.listener.api.packets.CloseWindowPacket;
import net.nonswag.tnl.listener.api.packets.CooldownPacket;
import net.nonswag.tnl.listener.api.packets.CustomPayloadPacket;
import net.nonswag.tnl.listener.api.packets.EntityAnimationPacket;
import net.nonswag.tnl.listener.api.packets.EntityAttachPacket;
import net.nonswag.tnl.listener.api.packets.EntityDestroyPacket;
import net.nonswag.tnl.listener.api.packets.EntityEquipmentPacket;
import net.nonswag.tnl.listener.api.packets.EntityHeadRotationPacket;
import net.nonswag.tnl.listener.api.packets.EntityMetadataPacket;
import net.nonswag.tnl.listener.api.packets.EntitySpawnPacket;
import net.nonswag.tnl.listener.api.packets.EntityStatusPacket;
import net.nonswag.tnl.listener.api.packets.EntityTeleportPacket;
import net.nonswag.tnl.listener.api.packets.EntityVelocityPacket;
import net.nonswag.tnl.listener.api.packets.GameStateChangePacket;
import net.nonswag.tnl.listener.api.packets.LivingEntitySpawnPacket;
import net.nonswag.tnl.listener.api.packets.MapChunkPacket;
import net.nonswag.tnl.listener.api.packets.MountPacket;
import net.nonswag.tnl.listener.api.packets.NamedEntitySpawnPacket;
import net.nonswag.tnl.listener.api.packets.OpenSignPacket;
import net.nonswag.tnl.listener.api.packets.OpenWindowPacket;
import net.nonswag.tnl.listener.api.packets.PlayerInfoPacket;
import net.nonswag.tnl.listener.api.packets.SetSlotPacket;
import net.nonswag.tnl.listener.api.packets.TitlePacket;
import net.nonswag.tnl.listener.api.packets.UpdateTimePacket;
import net.nonswag.tnl.listener.api.packets.WindowDataPacket;
import net.nonswag.tnl.listener.api.packets.WindowItemsPacket;
import net.nonswag.tnl.listener.api.packets.WorldBorderPacket;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class PacketManager extends Mapping.Packets {

    @Nonnull
    @Override
    public BlockBreakAnimationPacket blockBreakAnimationPacket(@Nonnull BlockLocation location, int state) {
        return new NMSBlockBreakAnimationPacket(location, state);
    }

    @Nonnull
    @Override
    public BossBarPacket bossBarPacket(@Nonnull BossBarPacket.Action action, @Nonnull BossBar bossBar) {
        return new NMSBossBarPacket(action, bossBar);
    }

    @Nonnull
    @Override
    public CameraPacket cameraPacket(int i) {
        return new NMSCameraPacket(i);
    }

    @Nonnull
    @Override
    public ChatPacket chatPacket(@Nonnull String message, @Nonnull ChatPacket.Type type, @Nonnull UUID sender) {
        return new NMSChatPacket(message, type, sender);
    }

    @Nonnull
    @Override
    public CloseWindowPacket closeWindowPacket(int windowId) {
        return new NMSCloseWindowPacket(windowId);
    }

    @Nonnull
    @Override
    public CooldownPacket cooldownPacket(@Nonnull Material item, int cooldown) {
        return new NMSCooldownPacket(item, cooldown);
    }

    @Nonnull
    @Override
    public CustomPayloadPacket customPayloadPacket(@Nonnull String channel, @Nonnull byte[]... bytes) {
        return new NMSCustomPayloadPacket(channel, bytes);
    }

    @Nonnull
    @Override
    public EntityAnimationPacket entityAnimationPacket(int entityId, @Nonnull EntityAnimationPacket.Animation animation) {
        return new NMSEntityAnimationPacket(entityId, animation);
    }

    @Nonnull
    @Override
    public EntityAttachPacket entityAttachPacket(int holderId, int leashedId) {
        return new NMSEntityAttachPacket(holderId, leashedId);
    }

    @Nonnull
    @Override
    public EntityDestroyPacket entityDestroyPacket(int... destroyIds) {
        return new NMSEntityDestroyPacket(destroyIds);
    }

    @Nonnull
    @Override
    public EntityEquipmentPacket entityEquipmentPacket(int entityId, @Nonnull HashMap<SlotType, ItemStack> equipment) {
        return new NMSEntityEquipmentPacket(entityId, equipment);
    }

    @Nonnull
    @Override
    public GameStateChangePacket gameStateChangePacket(@Nonnull GameStateChangePacket.Identifier identifier, float state) {
        return new NMSGameStateChangePacket(identifier, state);
    }

    @Nonnull
    @Override
    public EntityStatusPacket entityStatusPacket(int entityId, @Nonnull EntityStatusPacket.Status status) {
        return new NMSEntityStatusPacket(entityId, status);
    }

    @Nonnull
    @Override
    public EntitySpawnPacket entitySpawnPacket(@Nonnull Entity entity) {
        return new NMSEntitySpawnPacket(entity);
    }

    @Nonnull
    @Override
    public <W> EntityMetadataPacket<W> entityMetadataPacket(int entityId, @Nonnull W dataWatcher, boolean updateAll) {
        return (EntityMetadataPacket<W>) new NMSEntityMetadataPacket(entityId, (DataWatcher) dataWatcher, updateAll);
    }

    @Nonnull
    @Override
    public EntityHeadRotationPacket entityHeadRotationPacket(int entityId, float yaw) {
        return new NMSEntityHeadRotationPacket(entityId, yaw);
    }

    @Nonnull
    @Override
    public EntityTeleportPacket entityTeleportPacket(int i, @Nonnull Position position) {
        return new NMSEntityTeleportPacket(i, position);
    }

    @Nonnull
    @Override
    public EntityVelocityPacket entityVelocityPacket(int entityId, @Nonnull Vector vector) {
        return new NMSEntityVelocityPacket(entityId, vector);
    }

    @Nonnull
    @Override
    public LivingEntitySpawnPacket livingEntitySpawnPacket(@Nonnull LivingEntity entity) {
        return new NMSLivingEntitySpawnPacket(entity);
    }

    @Nonnull
    @Override
    public MapChunkPacket mapChunkPacket(@Nonnull Chunk chunk, int section) {
        return new NMSMapChunkPacket(chunk, section);
    }

    @Nonnull
    @Override
    public MountPacket mountPacket(int holderId, int[] mounts) {
        return new NMSMountPacket(holderId, mounts);
    }

    @Nonnull
    @Override
    public NamedEntitySpawnPacket namedEntitySpawnPacket(@Nonnull HumanEntity player) {
        return new NMSNamedEntitySpawnPacket(player);
    }

    @Nonnull
    @Override
    public OpenSignPacket openSignPacket(@Nonnull BlockLocation location) {
        return new NMSOpenSignPacket(location);
    }

    @Nonnull
    @Override
    public OpenWindowPacket openWindowPacket(int windowId, @Nonnull OpenWindowPacket.Type type, @Nonnull String title) {
        return new NMSOpenWindowPacket(windowId, type, title);
    }

    @Nonnull
    @Override
    public PlayerInfoPacket playerInfoPacket(@Nonnull Player player, @Nonnull PlayerInfoPacket.Action action) {
        return new NMSPlayerInfoPacket(player, action);
    }

    @Nonnull
    @Override
    public SetSlotPacket setSlotPacket(@Nonnull SetSlotPacket.Inventory inventory, int slot, @Nullable ItemStack itemStack) {
        return new NMSSetSlotPacket(inventory, slot, itemStack);
    }

    @Nonnull
    @Override
    public TitlePacket titlePacket(@Nonnull TitlePacket.Action action, @Nullable String text, int timeIn, int timeStay, int timeOut) {
        return new NMSTitlePacket(action, text, timeIn, timeStay, timeOut);
    }

    @Nonnull
    @Override
    public UpdateTimePacket updateTimePacket(long age, long timestamp, boolean cycle) {
        return new NMSUpdateTimePacket(age, timestamp, cycle);
    }

    @Nonnull
    @Override
    public WindowDataPacket windowDataPacket(int windowId, int property, int value) {
        return new NMSWindowDataPacket(windowId, property, value);
    }

    @Nonnull
    @Override
    public WindowItemsPacket windowItemsPacket(int windowId, @Nonnull List<ItemStack> items) {
        return new NMSWindowItemsPacket(windowId, items);
    }

    @Nonnull
    @Override
    public WorldBorderPacket worldBorderPacket(@Nonnull VirtualBorder virtualBorder, @Nonnull WorldBorderPacket.Action action) {
        return new NMSWorldBorderPacket(virtualBorder, action);
    }
}

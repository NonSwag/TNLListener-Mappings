package net.nonswag.tnl.mappings.v1_16_R3.api.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.*;
import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.object.Pair;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.api.entity.TNLEntity;
import net.nonswag.tnl.listener.api.entity.TNLEntityLiving;
import net.nonswag.tnl.listener.api.entity.TNLEntityPlayer;
import net.nonswag.tnl.listener.api.location.BlockLocation;
import net.nonswag.tnl.listener.api.mods.labymod.LabyPlayer;
import net.nonswag.tnl.listener.api.mods.mysterymod.MysteryPlayer;
import net.nonswag.tnl.listener.api.packets.*;
import net.nonswag.tnl.listener.api.player.Skin;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.player.manager.*;
import net.nonswag.tnl.listener.api.player.npc.NPCFactory;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.events.PlayerPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class NMSPlayer extends TNLPlayer {

    public NMSPlayer(@Nonnull Player player) {
        super(player);
        gameProfile = new net.nonswag.tnl.listener.api.player.GameProfile(getUniqueId(), getRealName(), skinManager().getSkin());
    }

    @Nonnull
    private EntityPlayer nms() {
        return ((CraftPlayer) bukkit()).getHandle();
    }

    @Nonnull
    private PlayerConnection playerConnection() {
        return nms().playerConnection;
    }

    @Nonnull
    private NetworkManager networkManager() {
        return playerConnection().networkManager;
    }

    @Nonnull
    private WorldServer worldServer() {
        return ((CraftWorld) bukkit().getWorld()).getHandle();
    }

    @Override
    public void setName(@Nonnull String name) {
        GameProfile profile = nms().getProfile();
        Reflection.setField(profile, "name", name);
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(Bootstrap.getInstance(), bukkit());
            all.showPlayer(Bootstrap.getInstance(), bukkit());
        }
    }

    @Override
    public int getPing() {
        return nms().ping;
    }

    @Override
    public void setPing(int ping) {
        nms().ping = ping;
    }

    @Override
    public void sendPacket(@Nonnull PacketBuilder packet) {
        try {
            playerConnection().sendPacket(packet.build());
        } catch (Exception e) {
            Logger.error.println(e);
        }
    }

    @Nonnull
    @Override
    public PermissionManager permissionManager() {
        if (permissionManager == null) permissionManager = new PermissionManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return permissionManager;
    }

    @Nonnull
    @Override
    public DataManager data() {
        if (data == null) data = new DataManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return data;
    }

    @Nonnull
    @Override
    public LabyPlayer labymod() {
        if (labymod == null) labymod = new LabyPlayer() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return labymod;
    }

    @Nonnull
    @Override
    public MysteryPlayer mysterymod() {
        if (mysterymod == null) mysterymod = new MysteryPlayer() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return mysterymod;
    }

    @Nonnull
    @Override
    public SoundManager soundManager() {
        if (soundManager == null) soundManager = new SoundManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return soundManager;
    }

    @Nonnull
    @Override
    public NPCFactory npcFactory() {
        if (npcFactory == null) npcFactory = new NPCFactory() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return npcFactory;
    }

    @Nonnull
    @Override
    public Messenger messenger() {
        if (messenger == null) messenger = new Messenger() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return messenger;
    }

    @Nonnull
    @Override
    public ScoreboardManager scoreboardManager() {
        if (scoreboardManager == null) scoreboardManager = new ScoreboardManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return scoreboardManager;
    }

    @Nonnull
    @Override
    public InterfaceManager interfaceManager() {
        if (interfaceManager == null) interfaceManager = new InterfaceManager() {
            @Override
            public void openVirtualSignEditor(@Nonnull SignMenu signMenu) {
                closeGUI(false);
                Location loc = worldManager().getLocation();
                BlockLocation location = new BlockLocation(worldManager().getWorld(), loc.getBlockX(), loc.getBlockY() - 5, loc.getBlockZ());
                signMenu.setLocation(location);
                BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
                OpenSignPacket editor = OpenSignPacket.create(location);
                TileEntitySign tileEntitySign = new TileEntitySign();
                tileEntitySign.setLocation(worldServer(), position);
                for (int line = 0; line < signMenu.getLines().length; line++) {
                    tileEntitySign.lines[line] = new ChatMessage(Message.format(signMenu.getLines()[line], getPlayer()));
                }
                Material material = Material.getMaterial(signMenu.getType().name());
                worldManager().sendBlockChange(location, Objects.requireNonNullElse(material, Material.SPRUCE_WALL_SIGN).createBlockData());
                PacketPlayOutTileEntityData packet = tileEntitySign.getUpdatePacket();
                assert packet != null;
                sendPackets(PacketBuilder.of(packet), editor);
                this.signMenu = signMenu;
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return interfaceManager;
    }

    @Nonnull
    @Override
    public WorldManager worldManager() {
        if (worldManager == null) worldManager = new WorldManager() {
            @Override
            public boolean isInRain() {
                BlockPosition position1 = nms().getChunkCoordinates();
                BlockPosition position2 = new BlockPosition(position1.getX(), getBoundingBox().getMaxY(), position1.getZ());
                return worldServer().isRainingAt(position1) || worldServer().isRainingAt(position2);
            }

            @Override
            public void strikeLightning(@Nonnull Location location, boolean effect, boolean sound) {
                EntityLightning lightning = new EntityLightning(EntityTypes.LIGHTNING_BOLT, worldServer());
                lightning.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
                lightning.setEffect(effect);
                lightning.isSilent = !sound;
                lightning.tick();
                sendPacket(PacketBuilder.of(lightning.P()));
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return worldManager;
    }

    @Nonnull
    @Override
    public EnvironmentManager environmentManager() {
        if (environmentManager == null) environmentManager = new EnvironmentManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return environmentManager;
    }

    @Nonnull
    @Override
    public HealthManager healthManager() {
        if (healthManager == null) healthManager = new HealthManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return healthManager;
    }

    @Nonnull
    @Override
    public CombatManager combatManager() {
        if (combatManager == null) combatManager = new CombatManager() {
            @Override
            public void exitCombat() {
                nms().exitCombat();
            }

            @Override
            public void enterCombat() {
                nms().enterCombat();
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return combatManager;
    }

    @Nonnull
    @Override
    public SkinManager skinManager() {
        if (skinManager == null) skinManager = new SkinManager() {

            @Nullable
            private Skin skin = null;

            @Nonnull
            @Override
            public Skin getSkin() {
                if (this.skin == null) {
                    GameProfile profile = nms().getProfile();
                    Collection<Property> textures = profile.getProperties().get("textures");
                    for (Property texture : textures) {
                        this.skin = new Skin(texture.getValue(), texture.getSignature());
                        break;
                    }
                    this.skin = Skin.getSkin(getPlayer().getUniqueId());
                }
                return skin;
            }

            @Override
            public void disguise(@Nonnull TNLEntity entity, @Nonnull TNLPlayer receiver) {
                if (getPlayer().equals(receiver)) return;
                receiver.sendPacket(EntityDestroyPacket.create(getPlayer().bukkit()));
                int id = entity.getEntityId();
                receiver.sendPacket(EntityDestroyPacket.create(id));
                if (entity instanceof TNLEntityPlayer player) {
                    receiver.sendPacket(PlayerInfoPacket.create(player, PlayerInfoPacket.Action.REMOVE_PLAYER));
                    Reflection.setField(entity, Entity.class, "id", getPlayer().getEntityId());
                    receiver.sendPacket(PlayerInfoPacket.create(player, PlayerInfoPacket.Action.ADD_PLAYER));
                    receiver.sendPacket(NamedEntitySpawnPacket.create(player));
                } else if (entity instanceof TNLEntityLiving livingEntity) {
                    Reflection.setField(entity, Entity.class, "id", getPlayer().getEntityId());
                    receiver.sendPacket(LivingEntitySpawnPacket.create(livingEntity.bukkit()));
                    receiver.sendPacket(EntityEquipmentPacket.create(livingEntity.bukkit()));
                } else {
                    Reflection.setField(entity, Entity.class, "id", getPlayer().getEntityId());
                    receiver.sendPacket(EntitySpawnPacket.create(entity.bukkit()));
                }
                receiver.sendPacket(EntityMetadataPacket.create(entity.bukkit()));
                receiver.sendPacket(EntityHeadRotationPacket.create(entity.bukkit()));
                Reflection.setField(entity, Entity.class, "id", id);
            }

            @Override
            public void setCapeVisibility(boolean visible) {
                cape = visible;
                nms().getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) (cape ? 127 : 126));
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return skinManager;
    }

    @Nonnull
    @Override
    public InventoryManager inventoryManager() {
        if (inventoryManager == null) inventoryManager = new InventoryManager() {
            @Override
            public void dropItem(@Nonnull ItemStack item, @Nonnull Consumer<org.bukkit.entity.Item> after) {
                Bootstrap.getInstance().sync(() -> {
                    EntityItem drop = nms().drop(CraftItemStack.asNMSCopy(item), true);
                    if (!(drop instanceof org.bukkit.entity.Item)) return;
                    after.accept((org.bukkit.entity.Item) drop.getBukkitEntity());
                });
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return inventoryManager;
    }

    @Nonnull
    @Override
    public DebugManager debugManager() {
        if (debugManager == null) debugManager = new DebugManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return debugManager;
    }

    @Nonnull
    @Override
    public AttributeManager attributeManager() {
        if (attributeManager == null) attributeManager = new AttributeManager() {
            @Nonnull
            @Override
            public AttributeInstance getAttribute(@Nonnull Attribute attribute) {
                return new CraftAttributeInstance(nms().getAttributeMap().a(toMinecraft(attribute)), attribute);
            }

            @Nullable
            private AttributeBase toMinecraft(@Nonnull Attribute attribute) {
                return IRegistry.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey()));
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return attributeManager;
    }

    @Nonnull
    @Override
    public MetaManager metaManager() {
        if (metaManager == null) metaManager = new MetaManager() {
            @Override
            public void setFlag(int flag, boolean value) {
                nms().setFlag(flag, value);
            }

            @Override
            public boolean getFlag(int flag) {
                return nms().getFlag(flag);
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return metaManager;
    }

    @Nonnull
    @Override
    public EffectManager effectManager() {
        if (effectManager == null) effectManager = new EffectManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return effectManager;
    }

    @Nonnull
    @Override
    public AbilityManager abilityManager() {
        if (abilityManager == null) abilityManager = new AbilityManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return abilityManager;
    }

    @Nonnull
    @Override
    public ServerManager serverManager() {
        if (serverManager == null) serverManager = new ServerManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return serverManager;
    }

    @Nonnull
    @Override
    public CinematicManger cinematicManger() {
        if (cinematicManger == null) cinematicManger = new CinematicManger() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return cinematicManger;
    }

    @Nonnull
    @Override
    public TitleManager titleManager() {
        if (titleManager == null) titleManager = new TitleManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return titleManager;
    }

    @Nonnull
    @Override
    public ParticleManager particleManager() {
        if (particleManager == null) particleManager = new ParticleManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return particleManager;
    }

    @Nonnull
    @Override
    public BossBarManager bossBarManager() {
        if (bossBarManager == null) bossBarManager = new BossBarManager() {
            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return bossBarManager;
    }

    @Nonnull
    @Override
    public CooldownManager cooldownManager() {
        if (cooldownManager == null) cooldownManager = new CooldownManager() {
            @Override
            public void resetAttackCooldown() {
                nms().resetAttackCooldown();
            }

            @Nonnull
            @Override
            public TNLPlayer getPlayer() {
                return NMSPlayer.this;
            }
        };
        return cooldownManager;
    }

    @Override
    public boolean isInjected() {
        if (((CraftPlayer) bukkit()).getHandle().playerConnection == null) return false;
        return playerConnection().networkManager != null;
    }

    @Override
    public void inject() {
        try {
            ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object packetObject) {
                    try {
                        if (!handleInjections(packetObject)) return;
                        PlayerPacketEvent event = new PlayerPacketEvent(NMSPlayer.this, packetObject);
                        if (event.call()) super.channelRead(channelHandlerContext, event.getPacket());
                    } catch (Exception e) {
                        Logger.error.println(e);
                        uninject();
                    }
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object packetObject, ChannelPromise channelPromise) {
                    try {
                        if (!handleInjections(packetObject)) return;
                        PlayerPacketEvent event = new PlayerPacketEvent(NMSPlayer.this, packetObject);
                        if (event.call()) super.write(channelHandlerContext, event.getPacket(), channelPromise);
                    } catch (Exception e) {
                        Logger.error.println(e);
                        uninject();
                    }
                }

                private boolean handleInjections(@Nonnull Object packet) {
                    Iterator<Pair<Class<?>, Injection<?>>> iterator = getInjections().iterator();
                    boolean success = true;
                    while (iterator.hasNext()) {
                        Pair<Class<?>, Injection<?>> pair = iterator.next();
                        Injection<Object> injection = (Injection<Object>) pair.getValue();
                        if (injection != null) {
                            if (!pair.getKey().equals(packet.getClass())) continue;
                            try {
                                injection.run(NMSPlayer.this, packet);
                                if (injection.isCancelled()) success = false;
                            } catch (Throwable t) {
                                Logger.error.println(t);
                            } finally {
                                Injection.After after = injection.getAfter();
                                if (after != null) after.run(NMSPlayer.this);
                                if (injection.isForRemove()) iterator.remove();
                            }
                        } else iterator.remove();
                    }
                    return success;
                }
            };
            ChannelPipeline pipeline = networkManager().channel.pipeline();
            try {
                pipeline.addBefore("packet_handler", getName() + "-TNLListener", channelDuplexHandler);
            } catch (Throwable ignored) {
                uninject();
            }
        } catch (Exception e) {
            uninject();
            Logger.error.println(e);
        }
    }

    @Override
    public void uninject() {
        try {
            Channel channel = networkManager().channel;
            if (channel.pipeline().get(getName() + "-TNLListener") != null) {
                channel.eventLoop().submit(() -> channel.pipeline().remove(getName() + "-TNLListener"));
            }
            getInjections().clear();
            data().export();
        } catch (Exception ignored) {
        } finally {
            players.remove(bukkit());
        }
    }
}

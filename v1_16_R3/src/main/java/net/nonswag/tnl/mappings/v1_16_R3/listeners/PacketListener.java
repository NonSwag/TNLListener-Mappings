package net.nonswag.tnl.mappings.v1_16_R3.listeners;

import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.*;
import net.nonswag.tnl.core.api.file.helper.JsonHelper;
import net.nonswag.tnl.core.api.language.Language;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.object.Objects;
import net.nonswag.tnl.core.api.reflection.Reflection;
import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.gui.AnvilGUI;
import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.gui.Interaction;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.holograms.event.InteractEvent;
import net.nonswag.tnl.listener.api.packets.OpenWindowPacket;
import net.nonswag.tnl.listener.api.packets.SetSlotPacket;
import net.nonswag.tnl.listener.api.player.manager.ResourceManager;
import net.nonswag.tnl.listener.api.player.npc.FakePlayer;
import net.nonswag.tnl.listener.api.serializer.ModPacketSerializer;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.listener.api.sign.SignMenu;
import net.nonswag.tnl.listener.events.*;
import net.nonswag.tnl.listener.events.mods.labymod.LabyPlayerMessageEvent;
import net.nonswag.tnl.listener.events.mods.mysterymod.MysteryPlayerMessageEvent;
import net.nonswag.tnl.mappings.v1_16_R3.api.player.NMSPlayer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class PacketListener implements Listener {

    @EventHandler
    public void onPacket(@Nonnull PlayerPacketEvent event) {
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat packet) {
                PlayerChatEvent chatEvent = new PlayerChatEvent(event.getPlayer(), packet.b());
                if (Settings.BETTER_CHAT.getValue() && !chatEvent.isCommand()) {
                    event.getPlayer().messenger().chat(chatEvent);
                    event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayInClientCommand packet) {
                PacketPlayInClientCommand.EnumClientCommand clientCommand = packet.b();
                if (clientCommand.equals(PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS)) {
                    event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayInSettings packet) {
                Language language = Language.fromLocale(packet.locale);
                Language old = event.getPlayer().data().getLanguage();
                if (!language.equals(Language.UNKNOWN) && !language.equals(old)) {
                    event.getPlayer().data().setLanguage(language);
                    new PlayerLanguageChangeEvent(event.getPlayer(), old).call();
                }
            } else if (event.getPacket() instanceof PacketPlayInCustomPayload packet) {
                event.setCancelled(true);
                String namespace = packet.tag.getNamespace();
                if (!namespace.equals("labymod3") && !namespace.equals("mysterymod")) return;
                try {
                    byte[] data = new byte[packet.data.readableBytes()];
                    packet.data.readBytes(data);
                    ByteBuf buf = Unpooled.wrappedBuffer(data);
                    if (namespace.equals("labymod3")) {
                        String key = ModPacketSerializer.readString(buf, Short.MAX_VALUE);
                        JsonElement message = JsonHelper.parse(ModPacketSerializer.readString(buf, Short.MAX_VALUE));
                        new LabyPlayerMessageEvent(event.getPlayer().labymod(), packet.tag.getKey(), key, message).call();
                    } else {
                        String key = ModPacketSerializer.readString(buf, Short.MAX_VALUE);
                        JsonElement message = JsonHelper.parse(key);
                        new MysteryPlayerMessageEvent(event.getPlayer().mysterymod(), packet.tag.getKey(), key, message).call();
                    }
                } catch (Exception e) {
                    Logger.error.println("An error occurred while reading a mod message from <'" + namespace + "'>", e);
                }
            } else if (event.getPacket() instanceof PacketPlayInUseEntity packet) {
                Entity entity = packet.a(((CraftWorld) event.getPlayer().worldManager().getWorld()).getHandle());
                if (entity != null) {
                    if (!event.getPlayer().delay("entity-interact", 50)) return;
                    TNLEvent entityEvent;
                    if (packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                        entityEvent = new EntityDamageByPlayerEvent(event.getPlayer(), entity.getBukkitEntity());
                    } else {
                        entityEvent = new PlayerInteractAtEntityEvent(event.getPlayer(), entity.getBukkitEntity());
                    }
                    if (!entityEvent.call()) event.setCancelled(true);
                } else {
                    Objects<Integer> id = event.getPacketField("a", Integer.class);
                    if (!id.hasValue()) return;
                    FakePlayer fakePlayer = event.getPlayer().npcFactory().getFakePlayer(id.nonnull());
                    if (fakePlayer != null) {
                        if (!event.getPlayer().delay("fakeplayer-interact", 50)) return;
                        FakePlayer.InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                FakePlayer.InteractEvent.Type.LEFT_CLICK : FakePlayer.InteractEvent.Type.RIGHT_CLICK;
                        fakePlayer.onInteract().accept(new FakePlayer.InteractEvent(event.getPlayer(), fakePlayer, type));
                    } else {
                        if (!event.getPlayer().delay("hologram-interact", 50)) return;
                        InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                InteractEvent.Type.LEFT_CLICK : InteractEvent.Type.RIGHT_CLICK;
                        for (Hologram hologram : Hologram.cachedValues()) {
                            for (Integer integer : hologram.getIds(event.getPlayer())) {
                                if (!integer.equals(id.nonnull())) continue;
                                InteractEvent interactEvent = new InteractEvent(hologram, event.getPlayer(), type);
                                hologram.onInteract().accept(interactEvent);
                                return;
                            }
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockDig) {
                PlayerDamageBlockEvent.BlockDamageType damageType = PlayerDamageBlockEvent.BlockDamageType.fromString(((PacketPlayInBlockDig) event.getPacket()).d().name());
                BlockPosition position = ((PacketPlayInBlockDig) event.getPacket()).b();
                EnumDirection againstBlock = ((PacketPlayInBlockDig) event.getPacket()).c();
                Block block = new Location(event.getPlayer().worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                Block relative = block.getRelative(againstBlock.getAdjacentX(), againstBlock.getAdjacentY(), againstBlock.getAdjacentZ());
                if (relative.getType().equals(Material.FIRE)) {
                    position = new BlockPosition(relative.getX(), relative.getY(), relative.getZ());
                    block = new Location(event.getPlayer().worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                }
                PlayerDamageBlockEvent blockEvent = new PlayerDamageBlockEvent(event.getPlayer(), block, damageType);
                event.setCancelled(!blockEvent.call());
                if (blockEvent.isCancelled()) {
                    if (blockEvent.getBlockDamageType().isInteraction(false)) {
                        Bootstrap.getInstance().sync(() -> {
                            for (BlockFace blockFace : BlockFace.values()) {
                                Block rel = blockEvent.getBlock().getRelative(blockFace);
                                event.getPlayer().worldManager().sendBlockChange(rel.getLocation(), rel.getBlockData());
                                rel.getState().update(true, false);
                            }
                        });
                    } else if (blockEvent.getBlockDamageType().isItemAction()) {
                        event.getPlayer().inventoryManager().updateInventory();
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInTabComplete) {
                String[] args = ((PacketPlayInTabComplete) event.getPacket()).c().split(" ");
                int index = ((PacketPlayInTabComplete) event.getPacket()).b();
                if (args.length == 0) event.setCancelled(true);
                else if (args[0].startsWith("/")) {
                    if (!Settings.TAB_COMPLETER.getValue() && !event.getPlayer().permissionManager().hasPermission(Settings.TAB_COMPLETE_BYPASS_PERMISSION.getValue())) {
                        event.setCancelled(true);
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockPlace) {
                org.bukkit.inventory.ItemStack itemStack = null;
                if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                    itemStack = event.getPlayer().inventoryManager().getInventory().getItemInMainHand();
                } else if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.OFF_HAND)) {
                    itemStack = event.getPlayer().inventoryManager().getInventory().getItemInOffHand();
                }
                if (itemStack == null || !itemStack.getType().equals(Material.GLASS_BOTTLE)) return;
                Block target = event.getPlayer().worldManager().getTargetBlock(5, FluidCollisionMode.ALWAYS);
                if (!(target != null && (target.getType().equals(Material.WATER)
                        || (target.getBlockData() instanceof Waterlogged
                        && ((Waterlogged) target.getBlockData()).isWaterlogged())
                        || target.getType().equals(Material.KELP)
                        || target.getType().equals(Material.KELP_PLANT)))) {
                    for (int i = 0; i < 6; i++) {
                        target = event.getPlayer().worldManager().getTargetBlock(i, FluidCollisionMode.ALWAYS);
                        if (target != null && (target.getType().equals(Material.WATER)
                                || (target.getBlockData() instanceof Waterlogged
                                && ((Waterlogged) target.getBlockData()).isWaterlogged())
                                || target.getType().equals(Material.KELP)
                                || target.getType().equals(Material.KELP_PLANT))) {
                            break;
                        }
                    }
                }
                if (target != null && (target.getType().equals(Material.WATER)
                        || (target.getBlockData() instanceof Waterlogged
                        && ((Waterlogged) target.getBlockData()).isWaterlogged())
                        || target.getType().equals(Material.KELP)
                        || target.getType().equals(Material.KELP_PLANT))) {
                    PlayerBottleFillEvent e = new PlayerBottleFillEvent(event.getPlayer(), itemStack, target);
                    if (!e.call()) {
                        event.setCancelled(true);
                        event.getPlayer().inventoryManager().updateInventory();
                    }
                    if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.MAIN_HAND)) {
                        event.getPlayer().inventoryManager().getInventory().setItemInMainHand(e.getItemStack());
                    } else if (((PacketPlayInBlockPlace) event.getPacket()).b().equals(EnumHand.OFF_HAND)) {
                        event.getPlayer().inventoryManager().getInventory().setItemInOffHand(e.getItemStack());
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInUpdateSign) {
                SignMenu menu = event.getPlayer().interfaceManager().getSignMenu();
                if (menu == null) return;
                event.setCancelled(true);
                if (menu.getResponse() != null) {
                    Bootstrap.getInstance().sync(() -> {
                        boolean success = menu.getResponse().test(event.getPlayer(), ((PacketPlayInUpdateSign) event.getPacket()).c());
                        if (!success && menu.isReopenOnFail()) {
                            event.getPlayer().interfaceManager().openVirtualSignEditor(menu);
                        }
                    });
                }
                if (menu.getLocation() != null) event.getPlayer().worldManager().sendBlockChange(menu.getLocation());
                event.getPlayer().interfaceManager().closeSignMenu();
            } else if (event.getPacket() instanceof PacketPlayInItemName packet) {
                GUI gui = event.getPlayer().interfaceManager().getGUI();
                if (!(gui instanceof AnvilGUI anvil)) return;
                event.setCancelled(true);
                for (AnvilGUI.TextInputEvent textInputEvent : anvil.getTextInputEvents()) {
                    textInputEvent.onTextInput(event.getPlayer(), packet.b() == null ? "" : packet.b());
                }
            } else if (event.getPacket() instanceof PacketPlayInResourcePackStatus packet) {
                ((NMSPlayer) event.getPlayer()).resourceManager().setStatus(switch (packet.status) {
                    case ACCEPTED -> ResourceManager.Status.ACCEPTED;
                    case DECLINED -> ResourceManager.Status.DECLINED;
                    case FAILED_DOWNLOAD -> ResourceManager.Status.FAILED_DOWNLOAD;
                    case SUCCESSFULLY_LOADED -> ResourceManager.Status.SUCCESSFULLY_LOADED;
                });
            } else if (event.getPacket() instanceof PacketPlayInWindowClick packet) {
                GUI gui = event.getPlayer().interfaceManager().getGUI();
                if (gui == null) return;
                int slot = packet.c();
                if (slot < gui.getSize() && slot >= 0) {
                    Interaction.Type type = Interaction.Type.fromNMS(packet.d(), packet.g().name());
                    gui.getClickListener().onClick(event.getPlayer(), slot, type);
                    GUIItem item = gui.getItem(slot);
                    if (item != null) for (Interaction interaction : item.getInteractions(type)) {
                        interaction.getAction().accept(event.getPlayer());
                    }
                } else if (slot >= gui.getSize()) {
                    event.setPacketField("slot", slot - gui.getSize() + 9);
                    event.setPacketField("a", 0);
                }
                event.setCancelled(true);
                event.reply(SetSlotPacket.create(SetSlotPacket.Inventory.COURSER, -1, null));
                event.getPlayer().inventoryManager().updateInventory();
                event.getPlayer().interfaceManager().updateGUI();
            } else if (event.getPacket() instanceof PacketPlayInCloseWindow) {
                GUI gui = event.getPlayer().interfaceManager().getGUI();
                if (gui == null) return;
                event.setCancelled(true);
                if (!gui.getCloseListener().onClose(event.getPlayer(), false)) {
                    event.reply(OpenWindowPacket.create(gui.getSize() / 9, Message.format(gui.getTitle())));
                    event.getPlayer().interfaceManager().updateGUI(gui);
                } else {
                    if (gui.getCloseSound() != null) event.getPlayer().soundManager().playSound(gui.getCloseSound());
                    event.getPlayer().interfaceManager().closeGUI(false);
                }
            } else if (event.getPacket() instanceof PacketPlayInPickItem packet) {
                PlayerItemPickEvent pickEvent = new PlayerItemPickEvent(event.getPlayer(), packet.b());
                if (!pickEvent.call()) event.setCancelled(true);
            } else if (event.getPacket() instanceof PacketPlayInUseItem packet) {
                BlockPosition position = packet.c().getBlockPosition();
                Block block = new Location(event.getPlayer().worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                if (block.getLocation().distance(event.getPlayer().worldManager().getLocation()) > 10) {
                    event.setCancelled(true);
                    return;
                }
                final EnumDirection direction = ((PacketPlayInUseItem) event.getPacket()).c().getDirection();
                BlockFace face = event.getPlayer().worldManager().getFacing().getOppositeFace();
                try {
                    face = BlockFace.valueOf(direction.name());
                } catch (Exception ignored) {
                } finally {
                    org.bukkit.inventory.ItemStack itemStack;
                    if (packet.b().equals(EnumHand.MAIN_HAND)) {
                        itemStack = event.getPlayer().inventoryManager().getInventory().getItemInMainHand();
                    } else itemStack = event.getPlayer().inventoryManager().getInventory().getItemInOffHand();
                    PlayerInteractEvent interactEvent = new PlayerInteractEvent(event.getPlayer(), block, face, itemStack);
                    if (!interactEvent.call()) {
                        event.setCancelled(true);
                        interactEvent.getPlayer().inventoryManager().updateInventory();
                    }
                    Bootstrap.getInstance().sync(() -> {
                        for (BlockFace f : BlockFace.values()) {
                            event.getPlayer().worldManager().sendBlockChange(interactEvent.getClickedBlock().getRelative(f));
                        }
                    }, 1);
                }
            }
        } else if (event.isOutgoing()) {
            if (event.getPacket() instanceof PacketPlayOutSpawnEntity) {
                Objects<EntityTypes<?>> k = ((Objects<EntityTypes<?>>) event.getPacketField("k"));
                if (!k.hasValue()) return;
                if (Settings.BETTER_TNT.getValue()) if (k.nonnull().equals(EntityTypes.TNT)) event.setCancelled(true);
                if (Settings.BETTER_FALLING_BLOCKS.getValue()) {
                    if (k.nonnull().equals(EntityTypes.FALLING_BLOCK)) event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayOutResourcePackSend packet) {
                String url = event.getPacketField("a", String.class).nonnull();
                String hash = event.getPacketField("b", String.class).nonnull();
                ((NMSPlayer) event.getPlayer()).resourceManager().setResourcePackUrl(url);
                ((NMSPlayer) event.getPlayer()).resourceManager().setResourcePackHash(hash);
            } else if (event.getPacket() instanceof PacketPlayOutCloseWindow) {
                GUI gui = event.getPlayer().interfaceManager().getGUI();
                if (gui == null) return;
                if (gui.getCloseSound() != null) event.getPlayer().soundManager().playSound(gui.getCloseSound());
                gui.getCloseListener().onClose(event.getPlayer(), true);
            } else if (event.getPacket() instanceof PacketPlayOutRespawn) {
                ResourceKey<World> key = event.getPacketField("b", ResourceKey.class).nonnull();
                MinecraftKey minecraftKey = Reflection.getField(key, MinecraftKey.class, "c").nonnull();
                Reflection.setField(minecraftKey, "namespace", "thenextlvl");
            } else if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
                int id = ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1);
                byte b = ((Objects<Byte>) event.getPacketField("b")).getOrDefault((byte) -1);
                if (event.getPlayer().getEntityId() == id) if (b >= 24 && b < 28) event.setPacketField("b", (byte) 28);
            }
        }
    }
}

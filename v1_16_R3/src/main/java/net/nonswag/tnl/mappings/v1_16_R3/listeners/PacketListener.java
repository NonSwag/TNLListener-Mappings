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
import net.nonswag.tnl.listener.Bootstrap;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.gui.AnvilGUI;
import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.gui.Interaction;
import net.nonswag.tnl.listener.api.holograms.Hologram;
import net.nonswag.tnl.listener.api.holograms.event.InteractEvent;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.packets.OpenWindowPacket;
import net.nonswag.tnl.listener.api.packets.SetSlotPacket;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
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
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class PacketListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPacket(@Nonnull PlayerPacketEvent event) {
        TNLPlayer player = event.getPlayer();
        if (event.isIncoming()) {
            if (event.getPacket() instanceof PacketPlayInChat packet) {
                PlayerChatEvent chatEvent = new PlayerChatEvent(player, packet.b());
                if (Settings.BETTER_CHAT.getValue() && !chatEvent.isCommand()) {
                    player.messenger().chat(chatEvent);
                    event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayInClientCommand packet) {
                PacketPlayInClientCommand.EnumClientCommand clientCommand = packet.b();
                if (clientCommand.equals(PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS)) {
                    event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayInSettings packet) {
                Language language = Language.fromLocale(packet.locale);
                Language old = player.data().getLanguage();
                if (!language.equals(Language.UNKNOWN) && !language.equals(old)) {
                    player.data().setLanguage(language);
                    new PlayerLanguageChangeEvent(player, old).call();
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
                        new LabyPlayerMessageEvent(player.labymod(), packet.tag.getKey(), key, message).call();
                    } else {
                        String key = ModPacketSerializer.readString(buf, Short.MAX_VALUE);
                        JsonElement message = JsonHelper.parse(key);
                        new MysteryPlayerMessageEvent(player.mysterymod(), packet.tag.getKey(), key, message).call();
                    }
                } catch (Exception e) {
                    Logger.error.println("An error occurred while reading a mod message from <'" + namespace + "'>", e);
                }
            } else if (event.getPacket() instanceof PacketPlayInUseEntity packet) {
                Entity entity = packet.a(((CraftWorld) player.worldManager().getWorld()).getHandle());
                if (entity != null) {
                    if (!player.delay("entity-interact", 50)) return;
                    TNLEvent entityEvent;
                    if (packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                        entityEvent = new EntityDamageByPlayerEvent(player, entity.getBukkitEntity());
                    } else {
                        entityEvent = new PlayerInteractAtEntityEvent(player, entity.getBukkitEntity());
                    }
                    if (!entityEvent.call()) event.setCancelled(true);
                } else {
                    Objects<Integer> id = event.getPacketField("a", Integer.class);
                    if (!id.hasValue()) return;
                    FakePlayer fakePlayer = player.npcFactory().getFakePlayer(id.nonnull());
                    if (fakePlayer != null) {
                        if (!player.delay("fakeplayer-interact", 50)) return;
                        FakePlayer.InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                FakePlayer.InteractEvent.Type.LEFT_CLICK : FakePlayer.InteractEvent.Type.RIGHT_CLICK;
                        fakePlayer.onInteract().accept(new FakePlayer.InteractEvent(player, fakePlayer, type));
                    } else {
                        if (!player.delay("hologram-interact", 50)) return;
                        InteractEvent.Type type = packet.b().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) ?
                                InteractEvent.Type.LEFT_CLICK : InteractEvent.Type.RIGHT_CLICK;
                        for (Hologram hologram : Hologram.cachedValues()) {
                            for (Integer integer : hologram.getIds(player)) {
                                if (!integer.equals(id.nonnull())) continue;
                                InteractEvent interactEvent = new InteractEvent(hologram, player, type);
                                hologram.onInteract().accept(interactEvent);
                                return;
                            }
                        }
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockDig packet) {
                PlayerDamageBlockEvent.BlockDamageType damageType = PlayerDamageBlockEvent.BlockDamageType.fromString(packet.d().name());
                if (damageType.isUnknown()) return;
                BlockPosition position = packet.b();
                Block block = new Location(player.worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                Block relative = block.getRelative(packet.c().getAdjacentX(), packet.c().getAdjacentY(), packet.c().getAdjacentZ());
                if (relative.getType().equals(Material.FIRE)) {
                    position = new BlockPosition(relative.getX(), relative.getY(), relative.getZ());
                    block = new Location(player.worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                }
                PlayerDamageBlockEvent blockEvent = new PlayerDamageBlockEvent(player, block, damageType);
                event.setCancelled(!blockEvent.call());
                if (blockEvent.isCancelled()) return;
                if (blockEvent.getBlockDamageType().isInteraction(false)) {
                    Bootstrap.getInstance().sync(() -> {
                        BlockFace[] faces = {BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.UP, BlockFace.DOWN};
                        for (BlockFace blockFace : faces) {
                            Block rel = blockEvent.getBlock().getRelative(blockFace);
                            player.worldManager().sendBlockChange(rel.getLocation(), rel.getBlockData());
                            rel.getState().update(true, false);
                        }
                    });
                } else if (blockEvent.getBlockDamageType().isItemAction()) {
                    player.inventoryManager().updateInventory();
                }
            } else if (event.getPacket() instanceof PacketPlayInTabComplete) {
                String[] args = ((PacketPlayInTabComplete) event.getPacket()).c().split(" ");
                int index = ((PacketPlayInTabComplete) event.getPacket()).b();
                if (args.length == 0) event.setCancelled(true);
                else if (args[0].startsWith("/")) {
                    if (!Settings.TAB_COMPLETER.getValue() && !player.permissionManager().hasPermission(Settings.TAB_COMPLETE_BYPASS_PERMISSION.getValue())) {
                        event.setCancelled(true);
                    }
                }
            } else if (event.getPacket() instanceof PacketPlayInBlockPlace packet) {
                org.bukkit.inventory.ItemStack itemStack = null;
                if (packet.b().equals(EnumHand.MAIN_HAND)) {
                    itemStack = player.inventoryManager().getInventory().getItemInMainHand();
                } else if (packet.b().equals(EnumHand.OFF_HAND)) {
                    itemStack = player.inventoryManager().getInventory().getItemInOffHand();
                }
                if (itemStack == null || !itemStack.getType().equals(Material.GLASS_BOTTLE)) return;
                Block target = player.worldManager().getTargetBlock(5, FluidCollisionMode.ALWAYS);
                if (!(target != null && (target.getType().equals(Material.WATER)
                        || (target.getBlockData() instanceof Waterlogged && ((Waterlogged) target.getBlockData()).isWaterlogged())
                        || target.getType().equals(Material.KELP) || target.getType().equals(Material.KELP_PLANT)))) {
                    for (int i = 0; i < 6; i++) {
                        target = player.worldManager().getTargetBlock(i, FluidCollisionMode.ALWAYS);
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
                        || (target.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged())
                        || target.getType().equals(Material.KELP) || target.getType().equals(Material.KELP_PLANT))) {
                    ItemStack itemStack1 = player.inventoryManager().getInventory().getItemInOffHand();
                    if (!itemStack.getType().equals(Material.GLASS_BOTTLE) && !itemStack1.getType().equals(Material.GLASS_BOTTLE)) {
                        return;
                    }
                    PlayerBottleFillEvent.Hand hand = packet.b().equals(EnumHand.MAIN_HAND) ? PlayerBottleFillEvent.Hand.MAIN_HAND : PlayerBottleFillEvent.Hand.OFF_HAND;
                    PlayerBottleFillEvent fillEvent = new PlayerBottleFillEvent(player, TNLItem.create(itemStack), target, hand);
                    if (fillEvent.getHand().isMainHand()) {
                        player.inventoryManager().getInventory().setItemInMainHand(fillEvent.getItemStack());
                    } else player.inventoryManager().getInventory().setItemInOffHand(fillEvent.getItemStack());
                    if (!fillEvent.call()) event.setCancelled(true);
                    if (fillEvent.getReplacement() == null) return;
                    var leftover = player.inventoryManager().getInventory().addItem(fillEvent.getReplacement());
                    player.inventoryManager().updateInventory();
                    if (leftover.isEmpty()) return;
                    Bootstrap.getInstance().sync(() -> leftover.values().forEach(item ->
                            player.worldManager().getWorld().dropItemNaturally(player.worldManager().getLocation(), item)));
                }
            } else if (event.getPacket() instanceof PacketPlayInUpdateSign) {
                SignMenu menu = player.interfaceManager().getSignMenu();
                if (menu == null) return;
                event.setCancelled(true);
                if (menu.getResponse() != null) {
                    Bootstrap.getInstance().sync(() -> {
                        boolean success = menu.getResponse().test(player, ((PacketPlayInUpdateSign) event.getPacket()).c());
                        if (!success && menu.isReopenOnFail()) {
                            player.interfaceManager().openVirtualSignEditor(menu);
                        }
                    });
                }
                if (menu.getLocation() != null) player.worldManager().sendBlockChange(menu.getLocation());
                player.interfaceManager().closeSignMenu();
            } else if (event.getPacket() instanceof PacketPlayInItemName packet) {
                GUI gui = player.interfaceManager().getGUI();
                if (!(gui instanceof AnvilGUI anvil)) return;
                event.setCancelled(true);
                for (AnvilGUI.TextInputEvent textInputEvent : anvil.getTextInputEvents()) {
                    textInputEvent.onTextInput(player, packet.b() == null ? "" : packet.b());
                }
            } else if (event.getPacket() instanceof PacketPlayInResourcePackStatus packet) {
                ((NMSPlayer) player).resourceManager().setStatus(switch (packet.status) {
                    case ACCEPTED -> ResourceManager.Status.ACCEPTED;
                    case DECLINED -> ResourceManager.Status.DECLINED;
                    case FAILED_DOWNLOAD -> ResourceManager.Status.FAILED_DOWNLOAD;
                    case SUCCESSFULLY_LOADED -> ResourceManager.Status.SUCCESSFULLY_LOADED;
                });
            } else if (event.getPacket() instanceof PacketPlayInWindowClick packet) {
                GUI gui = player.interfaceManager().getGUI();
                if (gui == null) return;
                int slot = packet.c();
                if (slot < gui.getSize() && slot >= 0) {
                    Interaction.Type type = Interaction.Type.fromNMS(packet.d(), packet.g().name());
                    gui.getClickListener().onClick(player, slot, type);
                    GUIItem item = gui.getItem(slot);
                    if (item != null) for (Interaction interaction : item.getInteractions(type)) {
                        interaction.getAction().accept(player);
                    }
                } else if (slot >= gui.getSize()) {
                    event.setPacketField("slot", slot - gui.getSize() + 9);
                    event.setPacketField("a", 0);
                }
                event.setCancelled(true);
                event.reply(SetSlotPacket.create(SetSlotPacket.Inventory.COURSER, -1, null));
                player.inventoryManager().updateInventory();
                player.interfaceManager().updateGUI();
            } else if (event.getPacket() instanceof PacketPlayInCloseWindow) {
                GUI gui = player.interfaceManager().getGUI();
                if (gui == null) return;
                event.setCancelled(true);
                if (!gui.getCloseListener().onClose(player, false)) {
                    event.reply(OpenWindowPacket.create(gui.getSize() / 9, Message.format(gui.getTitle())));
                    player.interfaceManager().updateGUI(gui);
                } else {
                    if (gui.getCloseSound() != null) player.soundManager().playSound(gui.getCloseSound());
                    player.interfaceManager().closeGUI(false);
                }
            } else if (event.getPacket() instanceof PacketPlayInPickItem packet) {
                PlayerItemPickEvent pickEvent = new PlayerItemPickEvent(player, packet.b());
                if (!pickEvent.call()) event.setCancelled(true);
            } else if (event.getPacket() instanceof PacketPlayInUseItem packet) {
                BlockPosition position = packet.c().getBlockPosition();
                Block block = new Location(player.worldManager().getWorld(), position.getX(), position.getY(), position.getZ()).getBlock();
                if (block.getLocation().distance(player.worldManager().getLocation()) > 10) {
                    event.setCancelled(true);
                    return;
                }
                final EnumDirection direction = ((PacketPlayInUseItem) event.getPacket()).c().getDirection();
                BlockFace face = player.worldManager().getFacing().getOppositeFace();
                try {
                    face = BlockFace.valueOf(direction.name());
                } catch (Exception ignored) {
                } finally {
                    org.bukkit.inventory.ItemStack itemStack;
                    if (packet.b().equals(EnumHand.MAIN_HAND)) {
                        itemStack = player.inventoryManager().getInventory().getItemInMainHand();
                    } else itemStack = player.inventoryManager().getInventory().getItemInOffHand();
                    PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, block, face, itemStack);
                    if (!interactEvent.call()) {
                        event.setCancelled(true);
                        interactEvent.getPlayer().inventoryManager().updateInventory();
                    }
                    Bootstrap.getInstance().sync(() -> {
                        for (BlockFace f : BlockFace.values()) {
                            player.worldManager().sendBlockChange(interactEvent.getClickedBlock().getRelative(f));
                        }
                    }, 1);
                }
            }
        } else if (event.isOutgoing()) {
            if (event.getPacket() instanceof PacketPlayOutSpawnEntity) {
                var k = event.getPacketField("k", EntityTypes.class);
                if (!k.hasValue()) return;
                if (Settings.BETTER_TNT.getValue()) if (k.nonnull().equals(EntityTypes.TNT)) event.setCancelled(true);
                if (Settings.BETTER_FALLING_BLOCKS.getValue()) {
                    if (k.nonnull().equals(EntityTypes.FALLING_BLOCK)) event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PacketPlayOutResourcePackSend packet) {
                String url = event.getPacketField("a", String.class).nonnull();
                String hash = event.getPacketField("b", String.class).nonnull();
                ((NMSPlayer) player).resourceManager().setResourcePackUrl(url);
                ((NMSPlayer) player).resourceManager().setResourcePackHash(hash);
            } else if (event.getPacket() instanceof PacketPlayOutCloseWindow) {
                GUI gui = player.interfaceManager().getGUI();
                if (gui == null) return;
                if (gui.getCloseSound() != null) player.soundManager().playSound(gui.getCloseSound());
                gui.getCloseListener().onClose(player, true);
            /*
            } else if (event.getPacket() instanceof PacketPlayOutRespawn) {
                ResourceKey<World> key = event.getPacketField("b", ResourceKey.class).nonnull();
                MinecraftKey minecraftKey = Reflection.getField(key, MinecraftKey.class, "c").nonnull();
                Reflection.setField(minecraftKey, "namespace", "thenextlvl");
             */
            } else if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
                int id = ((Objects<Integer>) event.getPacketField("a")).getOrDefault(-1);
                byte b = ((Objects<Byte>) event.getPacketField("b")).getOrDefault((byte) -1);
                if (player.getEntityId() == id && b >= 24 && b < 28) event.setPacketField("b", (byte) 28);
            }
        }
    }
}

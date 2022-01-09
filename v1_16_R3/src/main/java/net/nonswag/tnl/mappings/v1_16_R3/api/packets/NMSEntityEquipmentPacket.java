package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.nonswag.tnl.listener.api.item.SlotType;
import net.nonswag.tnl.listener.api.packets.EntityEquipmentPacket;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NMSEntityEquipmentPacket extends EntityEquipmentPacket {

    NMSEntityEquipmentPacket(int entityId, @Nonnull HashMap<SlotType, ItemStack> equipment) {
        super(entityId, equipment);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityEquipment build() {
        List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipment = new ArrayList<>();
        getEquipment().forEach((slot, itemStack) -> equipment.add(new Pair<>(slot.nms(), CraftItemStack.asNMSCopy(itemStack))));
        return new PacketPlayOutEntityEquipment(getEntityId(), equipment);
    }
}

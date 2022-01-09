package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.nonswag.tnl.listener.api.packets.EntityDestroyPacket;

import javax.annotation.Nonnull;

public final class NMSEntityDestroyPacket extends EntityDestroyPacket {

    public NMSEntityDestroyPacket(int... destroyIds) {
        super(destroyIds);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityDestroy build() {
        return new PacketPlayOutEntityDestroy(getDestroyIds());
    }
}

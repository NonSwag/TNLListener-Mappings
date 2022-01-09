package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutUpdateTime;
import net.nonswag.tnl.listener.api.packets.UpdateTimePacket;

import javax.annotation.Nonnull;

public final class NMSUpdateTimePacket extends UpdateTimePacket {

    NMSUpdateTimePacket(long age, long timestamp, boolean cycle) {
        super(age, timestamp, cycle);
    }

    @Nonnull
    @Override
    public PacketPlayOutUpdateTime build() {
        return new PacketPlayOutUpdateTime(getAge(), getTimestamp(), isCycle());
    }
}

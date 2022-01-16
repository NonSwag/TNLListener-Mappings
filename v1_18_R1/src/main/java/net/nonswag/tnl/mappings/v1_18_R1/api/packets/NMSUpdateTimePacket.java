package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutUpdateTime;
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

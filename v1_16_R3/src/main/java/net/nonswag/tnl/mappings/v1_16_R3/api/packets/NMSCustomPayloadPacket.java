package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import net.nonswag.tnl.listener.api.packets.CustomPayloadPacket;

import javax.annotation.Nonnull;

public final class NMSCustomPayloadPacket extends CustomPayloadPacket {

    NMSCustomPayloadPacket(@Nonnull String channel, @Nonnull byte[]... bytes) {
        super(channel, bytes);
    }

    @Nonnull
    @Override
    public PacketPlayOutCustomPayload build() {
        return new PacketPlayOutCustomPayload(new MinecraftKey(getChannel()), new PacketDataSerializer(Unpooled.wrappedBuffer(getBytes())));
    }
}

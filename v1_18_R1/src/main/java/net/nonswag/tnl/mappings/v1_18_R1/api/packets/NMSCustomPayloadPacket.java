package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
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

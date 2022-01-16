package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.nonswag.tnl.listener.api.packets.MapChunkPacket;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_18_R1.CraftChunk;

import javax.annotation.Nonnull;

public final class NMSMapChunkPacket extends MapChunkPacket {

    NMSMapChunkPacket(@Nonnull Chunk chunk, int section) {
        super(chunk, section);
    }

    @Nonnull
    @Override
    public ClientboundLevelChunkPacketData build() {
        return new ClientboundLevelChunkPacketData(((CraftChunk) getChunk()).getHandle());
    }
}

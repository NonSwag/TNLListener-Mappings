package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk;
import net.nonswag.tnl.listener.api.packets.MapChunkPacket;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;

import javax.annotation.Nonnull;

public final class NMSMapChunkPacket extends MapChunkPacket {

    NMSMapChunkPacket(@Nonnull Chunk chunk, int section) {
        super(chunk, section);
    }

    @Nonnull
    @Override
    public PacketPlayOutMapChunk build() {
        return new PacketPlayOutMapChunk(((CraftChunk) getChunk()).getHandle(), getSection());
    }
}

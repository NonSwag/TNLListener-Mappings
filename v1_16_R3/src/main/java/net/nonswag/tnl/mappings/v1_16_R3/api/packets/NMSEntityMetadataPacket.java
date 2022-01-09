package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.nonswag.tnl.listener.api.packets.EntityMetadataPacket;

import javax.annotation.Nonnull;

public final class NMSEntityMetadataPacket extends EntityMetadataPacket<DataWatcher> {

    NMSEntityMetadataPacket(int entityId, @Nonnull DataWatcher dataWatcher, boolean updateAll) {
        super(entityId, dataWatcher, updateAll);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityMetadata build() {
        return new PacketPlayOutEntityMetadata(getEntityId(), getDataWatcher(), isUpdateAll());
    }
}

package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
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

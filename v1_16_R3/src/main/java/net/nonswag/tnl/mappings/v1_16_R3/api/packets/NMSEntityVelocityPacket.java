package net.nonswag.tnl.mappings.v1_16_R3.api.packets;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.nonswag.tnl.listener.api.packets.EntityVelocityPacket;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public final class NMSEntityVelocityPacket extends EntityVelocityPacket {

    NMSEntityVelocityPacket(int entityId, @Nonnull Vector vector) {
        super(entityId, vector);
    }

    @Nonnull
    @Override
    public PacketPlayOutEntityVelocity build() {
        Vec3D vec3D = new Vec3D(getVector().getX(), getVector().getY(), getVector().getZ());
        return new PacketPlayOutEntityVelocity(getEntityId(), vec3D);
    }
}

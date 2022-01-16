package net.nonswag.tnl.mappings.v1_18_R1.api.packets;

import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.world.phys.Vec3D;
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

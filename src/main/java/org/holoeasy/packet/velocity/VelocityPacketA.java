package org.holoeasy.packet.velocity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class VelocityPacketA implements IVelocityPacket {
    public static final VelocityPacketA INSTANCE = new VelocityPacketA();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer velocity(int entityId, int x, int y, int z) {
        return packet(PacketType.Play.Server.ENTITY_VELOCITY, packet -> {
            StructureModifier<Integer> integers = packet.getIntegers();
            integers.write(0, entityId);
            integers.write(1, x);
            integers.write(2, y);
            integers.write(3, z);
        });
    }
}

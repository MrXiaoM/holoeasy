package org.holoeasy.packet.rotate;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.compressAngle;
import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class RotatePacketA implements IRotatePacket {
    public static final RotatePacketA INSTANCE = new RotatePacketA();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer rotate(int entityId, double yaw) {
        return packet(PacketType.Play.Server.ENTITY_LOOK, packet -> {
            packet.getIntegers().write(0, entityId);
            packet.getBytes().write(0, compressAngle(yaw));
        });
    }
}

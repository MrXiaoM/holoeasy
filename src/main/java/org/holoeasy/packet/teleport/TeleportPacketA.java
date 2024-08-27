package org.holoeasy.packet.teleport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.compressAngle;
import static org.holoeasy.ext.fixCoordinate;
import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class TeleportPacketA implements ITeleportPacket {
    public static final TeleportPacketA INSTANCE = new TeleportPacketA();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.V1_8);
    }

    @Override
    public PacketContainer teleport(int entityId, Location location) {
        return packet(PacketType.Play.Server.ENTITY_TELEPORT, packet -> {
            StructureModifier<Integer> integers = packet.getIntegers();
            StructureModifier<Byte> bytes = packet.getBytes();
            integers.write(0, entityId);
            integers.write(1, fixCoordinate(location.getX()));
            integers.write(2, fixCoordinate(location.getY()));
            integers.write(3, fixCoordinate(location.getZ()));
            bytes.write(0, compressAngle(location.getYaw()));
            bytes.write(1, compressAngle(location.getPitch()));
            packet.getBooleans().write(0, false);
        });
    }
}

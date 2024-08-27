package org.holoeasy.packet.teleport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.compressAngle;
import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class TeleportPacketB implements ITeleportPacket {
    public static final TeleportPacketB INSTANCE = new TeleportPacketB();
    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_9, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer teleport(int entityId, Location location) {
        return packet(PacketType.Play.Server.ENTITY_TELEPORT, packet -> {
            StructureModifier<Double> doubles = packet.getDoubles();
            StructureModifier<Byte> bytes = packet.getBytes();
            packet.getIntegers().write(0, entityId);

            doubles.write(1, location.getX());
            doubles.write(2, location.getY());
            doubles.write(3, location.getZ());
            bytes.write(0, compressAngle(location.getYaw()));
            bytes.write(1, compressAngle(location.getPitch()));

            packet.getBooleans().write(0, false);
        });
    }
}

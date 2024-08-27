package org.holoeasy.packet.spawn;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.holoeasy.util.VersionEnum;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class SpawnPacketD implements ISpawnPacket {
    public static final SpawnPacketD INSTANCE = new SpawnPacketD();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_19, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer spawn(int entityId, EntityType entityType, Location location, @Nullable Plugin plugin) {
        return packet(PacketType.Play.Server.SPAWN_ENTITY, packet -> {
            StructureModifier<Double> doubles = packet.getDoubles();
            packet.getIntegers().write(0, entityId);

            packet.getEntityTypeModifier().write(0, entityType);

            packet.getUUIDs().write(0, UUID.randomUUID());

            doubles.write(0, location.getX());
            doubles.write(1, location.getY());
            doubles.write(2, location.getZ());
        });
    }
}

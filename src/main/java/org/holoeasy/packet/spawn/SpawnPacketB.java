package org.holoeasy.packet.spawn;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class SpawnPacketB implements ISpawnPacket {
    public static final SpawnPacketB INSTANCE = new SpawnPacketB();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_9, VersionEnum.V1_15);
    }

    @Override
    public PacketContainer spawn(int entityId, EntityType entityType, Location location, @Nullable Plugin plugin) {
        int extraData = 1;
        return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING, packet -> {
            packet.getModifier().writeDefaults();
            StructureModifier<Integer> integers = packet.getIntegers();
            StructureModifier<Double> doubles = packet.getDoubles();

            integers.write(0, entityId);
            integers.write(1, entityType == EntityType.ARMOR_STAND
                    ? VersionUtil.CLEAN_VERSION.armorstandId
                    : VersionUtil.CLEAN_VERSION.droppedItemId);
            integers.write(2, extraData);

            packet.getUUIDs().write(0, UUID.randomUUID());

            doubles.write(0, location.getX());
            doubles.write(1, location.getY());
            doubles.write(2, location.getZ());
        });
    }
}

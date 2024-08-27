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

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class SpawnPacketC implements ISpawnPacket {
    public static final SpawnPacketC INSTANCE = new SpawnPacketC();
    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_16, VersionEnum.V1_18);
    }

    @Override
    public PacketContainer spawn(int entityId, EntityType entityType, Location location, @Nullable Plugin plugin) {
        int extraData = 1;
        if(entityType == EntityType.ARMOR_STAND) {
            return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING, packet -> {
                packet.getModifier().writeDefaults();
                StructureModifier<Integer> integers = packet.getIntegers();
                StructureModifier<Double> doubles = packet.getDoubles();

                integers.write(0, entityId);
                integers.write(1, VersionUtil.CLEAN_VERSION.armorstandId);
                integers.write(2, extraData);

                packet.getUUIDs().write(0, UUID.randomUUID());

                doubles.write(0, location.getX());
                doubles.write(1, location.getY());
                doubles.write(2, location.getZ());
            });
        } else {
            return packet(PacketType.Play.Server.SPAWN_ENTITY, packet -> {
                packet.getModifier().writeDefaults();
                StructureModifier<Integer> integers = packet.getIntegers();
                StructureModifier<Double> doubles = packet.getDoubles();

                integers.write(0, entityId);
                packet.getEntityTypeModifier().write(0, EntityType.DROPPED_ITEM);

                packet.getUUIDs().write(0, UUID.randomUUID());

                doubles.write(0, location.getX());
                doubles.write(1, location.getY());
                doubles.write(2, location.getZ());

                integers.write(2, convertVelocity(0.0));
                integers.write(3, convertVelocity(0.0));
                integers.write(4, convertVelocity(0.0));
            });
        }

    }

    private int convertVelocity(double velocity) {
        /*
          Minecraft represents a velocity within 4 blocks per second, in any direction,
          by using the entire Short range, meaning you can only move up to 4 blocks/second
          on any given direction
        */
        return (int) (clamp(velocity, -3.9, 3.9) * 8000);
    }
    private double clamp(double targetNum, double min, double max) {
        // Makes sure a number is within a range
        return max(min, min(targetNum, max));
    }
}

package org.holoeasy.packet.spawn;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.holoeasy.packet.IPacket;
import org.jetbrains.annotations.Nullable;

public interface ISpawnPacket extends IPacket {
    default PacketContainer spawn(
            int entityId, EntityType entityType, Location location
    ) {
        return spawn(entityId, entityType, location, null);
    }

    PacketContainer spawn(
            int entityId, EntityType entityType, Location location,
            @Nullable Plugin plugin
    );
}

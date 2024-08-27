package org.holoeasy.line;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.holoeasy.packet.PacketType;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.holoeasy.ext.send;

public class Line {
    public static final AtomicInteger IDs_COUNTER = new AtomicInteger(new Random().nextInt());

    private final int entityID = IDs_COUNTER.getAndIncrement();
    private final Plugin plugin;
    private final EntityType entityType;
    private final PacketContainer entityDestroyPacket;
    private Location location;
    public Line(Plugin plugin, EntityType entityType, Location location) {
        this.plugin = plugin;
        this.entityType = entityType;
        this.location = location;
        this.entityDestroyPacket = PacketType.DELETE().delete(entityID);
    }
    public Line(Plugin plugin, EntityType entityType) {
        this(plugin, entityType, null);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getEntityID() {
        return entityID;
    }

    public void destroy(Player player) {
        send(player, entityDestroyPacket);
    }

    public void spawn(Player player) {
        if (location == null) throw new RuntimeException("Forgot the location?");
        PacketContainer packet = PacketType.SPAWN()
            .spawn(entityID, entityType, location, plugin);
        send(player, packet);
    }

    public void teleport(Player player) {
        if (location == null) throw new RuntimeException("Forgot the location?");
        PacketContainer packet = PacketType.TELEPORT()
            .teleport(entityID, location);
        send(player, packet);
    }
}

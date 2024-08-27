package org.holoeasy.packet.teleport;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.holoeasy.packet.IPacket;

public interface ITeleportPacket extends IPacket {
    PacketContainer teleport(int entityId, Location location);
}

package org.holoeasy.packet.velocity;

import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.packet.IPacket;

public interface IVelocityPacket extends IPacket {
    PacketContainer velocity(int entityId, int x, int y, int z);
}

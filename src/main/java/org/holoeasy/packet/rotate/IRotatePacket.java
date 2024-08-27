package org.holoeasy.packet.rotate;

import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.packet.IPacket;

public interface IRotatePacket extends IPacket {
    PacketContainer rotate(int entityId, double yaw);
}

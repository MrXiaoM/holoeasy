package org.holoeasy.packet.delete;

import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.packet.IPacket;

public interface IDeletePacket extends IPacket {
    PacketContainer delete(int entityId);
}

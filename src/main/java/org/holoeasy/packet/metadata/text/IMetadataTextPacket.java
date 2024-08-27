package org.holoeasy.packet.metadata.text;

import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.packet.IPacket;

public interface IMetadataTextPacket extends IPacket {
    default PacketContainer metadata(int entityId, String nameTag) {
        return metadata(entityId, nameTag, true);
    }
    PacketContainer metadata(int entityId, String nameTag, boolean invisible);
}

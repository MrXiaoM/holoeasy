package org.holoeasy.packet.metadata.text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.*;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class MetadataTextPacketD implements IMetadataTextPacket {
    public static final MetadataTextPacketD INSTANCE = new MetadataTextPacketD();
    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_19, VersionEnum.V1_19);
    }

    @Override
    public PacketContainer metadata(int entityId, String nameTag, boolean invisible) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        if (invisible) setByte(watcher, 0, (byte) 0x20);

        if (nameTag != null) {
            setChatComponent(watcher, 2, nameTag);
            setBool(watcher, 3, true);
        }

        // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
        parse119(packet, watcher);

        return packet;
    }
}

package org.holoeasy.packet.metadata.text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class MetadataTextPacketA implements IMetadataTextPacket {
    public static final MetadataTextPacketA INSTANCE = new MetadataTextPacketA();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.V1_8);
    }

    @Override
    public PacketContainer metadata(int entityId, String nameTag, boolean invisible) {
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        if (invisible) watcher.setObject(0, (byte) 0x20);

        if (nameTag != null) {
            watcher.setObject(2, nameTag);
            watcher.setObject(3, (byte) 1);
        }

        return packet(PacketType.Play.Server.ENTITY_METADATA, packet -> {
            packet.getIntegers().write(0, entityId);
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        });
    }
}

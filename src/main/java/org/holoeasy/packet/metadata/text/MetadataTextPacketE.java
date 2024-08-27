package org.holoeasy.packet.metadata.text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.holoeasy.util.VersionEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.holoeasy.util.ClosedRange.rangeSingle;
import static org.holoeasy.util.Serializers.BOOL_SERIALIZER;
import static org.holoeasy.util.Serializers.BYTE_SERIALIZER;

public class MetadataTextPacketE implements IMetadataTextPacket {
    public static final MetadataTextPacketE INSTANCE = new MetadataTextPacketE();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_20, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer metadata(int entityId, String nameTag, boolean invisible) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

        if (invisible) wrappedDataValueList.add(
                new WrappedDataValue(0, BYTE_SERIALIZER(), (byte) 0x20)
        );
        if (nameTag != null) {
            Optional<Object> opt = Optional.of(WrappedChatComponent.fromChatMessage(nameTag)[0].getHandle());

            wrappedDataValueList.add(new WrappedDataValue(2,
                    WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                    opt
            ));

            wrappedDataValueList.add(new WrappedDataValue(3, BOOL_SERIALIZER(), true));
        }

        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        return packet;
    }
}

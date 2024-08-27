package org.holoeasy.packet.metadata.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.bukkitGeneric;
import static org.holoeasy.util.ClosedRange.rangeSingle;
import static org.holoeasy.util.Serializers.BOOL_SERIALIZER;
import static org.holoeasy.util.Serializers.ITEM_SERIALIZER;

public class MetadataItemPacketE implements IMetadataItemPacket {
    public static final MetadataItemPacketE INSTANCE = new MetadataItemPacketE();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_19, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer metadata(int entityId, ItemStack item) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, entityId);
        packet.getDataValueCollectionModifier().write(0, Lists.newArrayList(
                new WrappedDataValue(5, BOOL_SERIALIZER(), true),
                new WrappedDataValue(8, ITEM_SERIALIZER(), bukkitGeneric(item))
        ));

        return packet;
    }
}

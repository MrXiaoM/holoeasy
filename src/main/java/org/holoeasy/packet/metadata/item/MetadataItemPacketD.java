package org.holoeasy.packet.metadata.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;
import java.util.*;

import static org.holoeasy.ext.bukkitGeneric;
import static org.holoeasy.ext.parse119;
import static org.holoeasy.util.ClosedRange.rangeSingle;
import static org.holoeasy.util.Serializers.BOOL_SERIALIZER;
import static org.holoeasy.util.Serializers.ITEM_SERIALIZER;


public class MetadataItemPacketD implements IMetadataItemPacket {
    public static final MetadataItemPacketD INSTANCE = new MetadataItemPacketD();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_19, VersionEnum.V1_19);
    }

    @Override
    public PacketContainer metadata(int entityId, ItemStack item) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        WrappedDataWatcherObject gravity = new WrappedDataWatcherObject(
            5, BOOL_SERIALIZER()
        );
        watcher.setObject(gravity, true);

        WrappedDataWatcherObject itemSer = new WrappedDataWatcherObject(
            8, ITEM_SERIALIZER()
        );
        watcher.setObject(itemSer, bukkitGeneric(item));

        // https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
        parse119(packet, watcher);

        return packet;
    }
}

package org.holoeasy.packet.metadata.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.ext.setBool;
import static org.holoeasy.ext.setItemStack;
import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;


public class MetadataItemPacketB implements IMetadataItemPacket {
    public static final MetadataItemPacketB INSTANCE = new MetadataItemPacketB();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_9, VersionEnum.V1_12);
    }

    @Override
    public PacketContainer metadata(int entityId, ItemStack item) {
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        setBool(watcher, 5, true);
        setItemStack(watcher, 6, item);

        return packet(PacketType.Play.Server.ENTITY_METADATA, packet -> {
            packet.getIntegers().write(0, entityId);
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        });
    }
}

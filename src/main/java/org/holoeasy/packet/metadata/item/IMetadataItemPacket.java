package org.holoeasy.packet.metadata.item;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.packet.IPacket;

public interface IMetadataItemPacket extends IPacket {
    PacketContainer metadata(int entityId, ItemStack item);
}

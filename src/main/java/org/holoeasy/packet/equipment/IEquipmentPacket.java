package org.holoeasy.packet.equipment;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.packet.IPacket;

public interface IEquipmentPacket extends IPacket {
    PacketContainer equip(int entityId, ItemStack helmet);
}

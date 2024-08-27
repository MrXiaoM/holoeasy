package org.holoeasy.packet.equipment;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class EquipmentPacketB implements IEquipmentPacket {
    public static final EquipmentPacketB INSTANCE = new EquipmentPacketB();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_9, VersionEnum.V1_12);
    }

    @Override
    public PacketContainer equip(int entityId, ItemStack helmet) {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT, packet -> {
            packet.getIntegers().write(0, entityId);
            packet.getItemSlots().write(0, EnumWrappers.ItemSlot.HEAD);
            packet.getItemModifier().write(0, helmet);
        });
    }
}

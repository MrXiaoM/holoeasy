package org.holoeasy.packet.equipment;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class EquipmentPacketA implements IEquipmentPacket {
    public static final EquipmentPacketA INSTANCE = new EquipmentPacketA();
    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.V1_8);
    }

    @Override
    public PacketContainer equip(int entityId, ItemStack helmet) {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT, packet -> {
            packet.getIntegers().write(0, entityId);

            // Use legacy form to update the head slot.
            packet.getIntegers().write(1, 4);

            packet.getItemModifier().write(0, helmet);
        });
    }
}

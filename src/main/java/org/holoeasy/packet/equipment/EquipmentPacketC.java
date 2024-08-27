package org.holoeasy.packet.equipment;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.util.VersionEnum;

import java.util.ArrayList;
import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class EquipmentPacketC implements IEquipmentPacket {
    public static final EquipmentPacketC INSTANCE = new EquipmentPacketC();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_13, VersionEnum.LATEST);
    }

    @Override
    public PacketContainer equip(int entityId, ItemStack helmet) {
        return packet(PacketType.Play.Server.ENTITY_EQUIPMENT, packet -> {
            packet.getIntegers().write(0, entityId);

            List<Pair<ItemSlot, ItemStack>> pairList = new ArrayList<>();
            pairList.add(new Pair<>(ItemSlot.HEAD, helmet));
            packet.getSlotStackPairLists().write(0, pairList);
        });
    }


}
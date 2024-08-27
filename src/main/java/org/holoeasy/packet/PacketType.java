package org.holoeasy.packet;

import org.holoeasy.HoloEasy;
import org.holoeasy.packet.delete.DeletePacketA;
import org.holoeasy.packet.delete.DeletePacketB;
import org.holoeasy.packet.delete.IDeletePacket;
import org.holoeasy.packet.equipment.EquipmentPacketA;
import org.holoeasy.packet.equipment.EquipmentPacketB;
import org.holoeasy.packet.equipment.EquipmentPacketC;
import org.holoeasy.packet.equipment.IEquipmentPacket;
import org.holoeasy.packet.metadata.item.*;
import org.holoeasy.packet.metadata.text.*;
import org.holoeasy.packet.rotate.IRotatePacket;
import org.holoeasy.packet.rotate.RotatePacketA;
import org.holoeasy.packet.spawn.*;
import org.holoeasy.packet.teleport.ITeleportPacket;
import org.holoeasy.packet.teleport.TeleportPacketA;
import org.holoeasy.packet.teleport.TeleportPacketB;
import org.holoeasy.packet.velocity.IVelocityPacket;
import org.holoeasy.packet.velocity.VelocityPacketA;

public class PacketType {
    @SafeVarargs
    private static <T extends IPacket> T getCurrImpl(T... impls) {
        T rightImpl = null;
        T lastImpl = null;
        for (T impl : impls) {
            if (rightImpl == null && impl.isCurrentVersion()) {
                rightImpl = impl;
            }
            lastImpl = impl;
        }
        if (rightImpl != null) {
            return rightImpl;
        }

        if (lastImpl != null && HoloEasy.useLastSupportedVersion) {
            return lastImpl;
        }

        throw new RuntimeException(
            "No version support for this packet\n" +
            "Set HoloEasy.useLastSupportedVersion to true or\n" +
            "open an issue at https://github.com/unldenis/holoeasy"
        );
    }

    private static IDeletePacket delete = null;
    public static IDeletePacket DELETE() {
        if (delete != null) return delete;
        return delete = getCurrImpl(
                DeletePacketA.INSTANCE,
                DeletePacketB.INSTANCE
        );
    }

    private static IMetadataTextPacket metadataText = null;
    public static IMetadataTextPacket METADATA_TEXT() {
        if (metadataText != null) return metadataText;
        return metadataText = getCurrImpl(
                MetadataTextPacketA.INSTANCE,
                MetadataTextPacketB.INSTANCE,
                MetadataTextPacketC.INSTANCE,
                MetadataTextPacketD.INSTANCE,
                MetadataTextPacketE.INSTANCE
        );
    }

    private static IMetadataItemPacket metadataItem = null;
    public static IMetadataItemPacket METADATA_ITEM() {
        if (metadataItem != null) return metadataItem;
        return metadataItem = getCurrImpl(
                MetadataItemPacketA.INSTANCE,
                MetadataItemPacketB.INSTANCE,
                MetadataItemPacketC.INSTANCE,
                MetadataItemPacketD.INSTANCE,
                MetadataItemPacketE.INSTANCE
        );
    }

    private static ISpawnPacket spawn = null;
    public static ISpawnPacket SPAWN() {
        if (spawn != null) return spawn;
        return spawn = getCurrImpl(
                SpawnPacketA.INSTANCE,
                SpawnPacketB.INSTANCE,
                SpawnPacketC.INSTANCE,
                SpawnPacketD.INSTANCE
        );
    }

    private static ITeleportPacket teleport = null;
    public static ITeleportPacket TELEPORT() {
        if (teleport != null) return teleport;
        return teleport = getCurrImpl(
                TeleportPacketA.INSTANCE,
                TeleportPacketB.INSTANCE
        );
    }

    private static IVelocityPacket velocity = null;
    public static IVelocityPacket VELOCITY() {
        if (velocity != null) return velocity;
        return velocity = getCurrImpl(VelocityPacketA.INSTANCE);
    }

    private static IRotatePacket rotate = null;
    public static IRotatePacket ROTATE() {
        if (rotate != null) return rotate;
        return rotate = getCurrImpl(RotatePacketA.INSTANCE);
    }

    private static IEquipmentPacket equipment = null;
    public static IEquipmentPacket EQUIPMENT() {
        if (equipment != null) return equipment;
        return equipment = getCurrImpl(
                EquipmentPacketA.INSTANCE,
                EquipmentPacketB.INSTANCE,
                EquipmentPacketC.INSTANCE
        );
    }
}

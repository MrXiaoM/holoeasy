package org.holoeasy.packet;

import com.google.common.collect.Lists;
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
import org.holoeasy.util.ClosedRange;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;

import java.util.List;

public interface IPacket {

    List<ClosedRange<VersionEnum>> versionSupport();

    default boolean isCurrentVersion() {
        for (ClosedRange<VersionEnum> range : versionSupport()) {
            if (VersionUtil.CLEAN_VERSION.in(range)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    static List<ClosedRange<VersionEnum>> list(ClosedRange<VersionEnum>... vararg) {
        return Lists.newArrayList(vararg);
    }
}



package org.holoeasy.packet.delete;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.holoeasy.util.ClosedRange;
import org.holoeasy.util.VersionEnum;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class DeletePacketA implements IDeletePacket {
    public static final DeletePacketA INSTANCE = new DeletePacketA();

    @Override
    public List<ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.V1_16);
    }

    @NotNull
    @Override
    public PacketContainer delete(int entityId) {
        return packet(PacketType.Play.Server.ENTITY_DESTROY, packet -> {
            packet.getIntegerArrays().write(0, new int[]{entityId});
        });
    }
}
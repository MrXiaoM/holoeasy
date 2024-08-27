package org.holoeasy.packet.delete;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import org.holoeasy.util.VersionEnum;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class DeletePacketB implements IDeletePacket {
    public static final DeletePacketB INSTANCE = new DeletePacketB();

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_17, VersionEnum.LATEST);
    }

    @NotNull
    @Override
    public PacketContainer delete(int entityId) {
        return packet(PacketType.Play.Server.ENTITY_DESTROY, packet -> {
            packet.getIntLists().write(0, Lists.newArrayList(entityId));
        });
    }
}

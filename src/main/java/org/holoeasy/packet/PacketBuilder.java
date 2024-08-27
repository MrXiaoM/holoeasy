package org.holoeasy.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import java.util.function.Consumer;

public class PacketBuilder {
    public static PacketContainer packet(PacketType type, Consumer<PacketContainer> initializer) {
        PacketContainer packet = new PacketContainer(type);
        initializer.accept(packet);
        return packet;
    }
}

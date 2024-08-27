package org.holoeasy;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.*;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ILine;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.floor;
import static org.holoeasy.util.Serializers.*;

public class ext {

    public static byte compressAngle(double d) {
        return (byte) (int) (d * 256f / 360f);
    }
    public static int fixCoordinate(double d) {
        return (int) floor(d * 32.0);
    }
    public static ILine<?> get(Hologram hologram, int index) {
        return hologram.lineAt(index);
    }
    public static Object bukkitGeneric(ItemStack item) {
        return BukkitConverters.getItemStackConverter().getGeneric(item);
    }

    public static void send(Player player, PacketContainer packet) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public static void parse119(PacketContainer packet, WrappedDataWatcher watcher) {
        if (MinecraftVersion.getCurrentVersion().isAtLeast(new MinecraftVersion("1.19.3"))) {
            List<WrappedDataValue> wrappedDataValueList = Lists.newArrayList();
            for (WrappedWatchableObject entry : watcher.getWatchableObjects()) {
                if (entry == null) continue;
                WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
                wrappedDataValueList.add(new WrappedDataValue(
                        dataWatcherObject.getIndex(),
                        dataWatcherObject.getSerializer(),
                        entry.getRawValue()
                ));
            }
            packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        } else {
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        }
    }
    public static <T> void set(StructureModifier<T> modifier, int index, T value) {
        modifier.write(index, value);
    }

    public static void setByte(WrappedDataWatcher watcher, int index, byte value) {
        WrappedDataWatcherObject obj = new WrappedDataWatcherObject(
                index,
                BYTE_SERIALIZER()
        );
        watcher.setObject(obj, value);
    }
    public static void setString(WrappedDataWatcher watcher, int index, String value) {
        WrappedDataWatcherObject obj = new WrappedDataWatcherObject(
                index,
                STRING_SERIALIZER()
        );
        watcher.setObject(obj, value);
    }
    public static void setBool(WrappedDataWatcher watcher, int index, boolean value) {
        WrappedDataWatcherObject obj = new WrappedDataWatcherObject(
                index,
                BOOL_SERIALIZER()
        );
        watcher.setObject(obj, value);
    }
    public static void setVectorSerializer(WrappedDataWatcher watcher, int index, Object value) {
        WrappedDataWatcherObject obj = new WrappedDataWatcherObject(
                index,
                WrappedDataWatcher.Registry.getVectorSerializer()
        );
        watcher.setObject(obj, value);
    }
    public static void setChatComponent(WrappedDataWatcher watcher, int index, String value) {
        Optional<Object> opt = Optional.of(WrappedChatComponent.fromChatMessage(value)[0].getHandle());
        watcher.setObject(new WrappedDataWatcherObject(
                index,
                WrappedDataWatcher.Registry.getChatComponentSerializer(true)
        ), opt);
    }

    public static void setItemStack(WrappedDataWatcher watcher, int index, ItemStack value) {
        WrappedDataWatcherObject obj = new WrappedDataWatcherObject(index, ITEM_SERIALIZER());
        watcher.setObject(obj, bukkitGeneric(value));
    }
}

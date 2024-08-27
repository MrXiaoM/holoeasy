package org.holoeasy.packet.spawn;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.holoeasy.util.BukkitFuture;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.holoeasy.packet.PacketBuilder.packet;
import static org.holoeasy.util.ClosedRange.rangeSingle;

public class SpawnPacketA implements ISpawnPacket {
    public static final SpawnPacketA INSTANCE = new SpawnPacketA();

    private CompletableFuture<Void> loadDefaultWatcher(Plugin plugin) {
        return BukkitFuture.runSync(plugin, () -> {
            World world = Bukkit.getWorlds().get(0);
            Entity entity = world.spawnEntity(new Location(world, 0.0, 256.0, 0.0), EntityType.ARMOR_STAND);
            defaultDataWatcher = WrappedDataWatcher.getEntityWatcher(entity);
            entity.remove();
        });
    }

    @Nullable
    private WrappedDataWatcher defaultDataWatcher = null;

    @Override
    public List<org.holoeasy.util.ClosedRange<VersionEnum>> versionSupport() {
        return rangeSingle(VersionEnum.V1_8, VersionEnum.V1_8);
    }

    @Override
    public PacketContainer spawn(int entityId, EntityType entityType, Location location, Plugin plugin) {
        return packet(PacketType.Play.Server.SPAWN_ENTITY_LIVING, packet -> {
            StructureModifier<Integer> integers = packet.getIntegers();
            integers.write(0, entityId);

            integers.write(1, VersionUtil.CLEAN_VERSION.armorstandId);

            integers.write(2, (int) (location.getX() * 32));
            integers.write(3, (int) (location.getY() * 32));
            integers.write(4, (int) (location.getZ() * 32));

            if (defaultDataWatcher == null) {
                if (plugin == null) throw new RuntimeException("Plugin cannot be null");
                loadDefaultWatcher(plugin).join();
            }

            packet.getDataWatcherModifier().write(0, defaultDataWatcher);
        });
    }
}

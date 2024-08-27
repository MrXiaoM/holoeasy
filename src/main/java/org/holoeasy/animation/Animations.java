package org.holoeasy.animation;

import com.comphenix.protocol.events.PacketContainer;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ILine;
import org.holoeasy.packet.PacketType;

import java.util.function.Function;

import static org.holoeasy.ext.send;

public enum Animations {

    CIRCLE(line -> {
        Hologram holo = line.getPvt().getHologram();
        AtomicDouble yaw = new AtomicDouble(0.0);

        return Bukkit.getScheduler().runTaskTimerAsynchronously(line.getPlugin(), () -> {
            PacketContainer packet = PacketType.ROTATE().rotate(line.getEntityId(), yaw.get());

            for (Player player : holo.getSeeingPlayers()) {
                send(player, packet);
            }

            yaw.addAndGet(10);
        }, 2, 2);
    });

    public final Function<ILine<?>, BukkitTask> task;

    Animations(Function<ILine<?>, BukkitTask> task) {
        this.task = task;
    }
}
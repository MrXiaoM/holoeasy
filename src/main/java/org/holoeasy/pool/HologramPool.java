package org.holoeasy.pool;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HologramPool implements Listener, IHologramPool {
    Map<UUID, Hologram> holograms = new ConcurrentHashMap<>();
    Plugin plugin;
    double spawnDistance;
    public HologramPool(Plugin plugin, double spawnDistance)  {
        this.plugin = plugin;
        this.spawnDistance = spawnDistance;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        hologramTick();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Hologram get(UUID id) {
        Hologram hologram = holograms.get(id);
        if (hologram == null) throw new NoValueForKeyException(id);
        return hologram;
    }

    @Override
    public void takeCareOf(Hologram value) {
        if (holograms.containsKey(value.getId())) {
            throw new KeyAlreadyExistsException(value.getId());
        }
        holograms.put(value.getId(), value);
    }

    @Nullable
    @Override
    public Hologram remove(UUID id) {
        // if removed
        Hologram removed = holograms.remove(id);
        if (removed != null) {
            for (Player player : removed.getSeeingPlayers()) {
                removed.hide(player);
            }
        }
        return removed;
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        for (Hologram it : holograms.values()) {
            if (it.isShownFor(player)) {
                it.hide(player);
            }
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Hologram it : holograms.values()) {
            if (it.isShownFor(player)) {
                it.getSeeingPlayers().remove(player);
            }
        }
    }

    /**
     * Starts the hologram tick.
     */
    private void hologramTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            for (Player player : Collections.unmodifiableCollection(Bukkit.getOnlinePlayers())) {
                for (Hologram hologram : this.holograms.values()) {
                    Location holoLoc = hologram.getLocation();
                    Location playerLoc = player.getLocation();
                    boolean isShown = hologram.isShownFor(player);

                    World holoWorld = holoLoc.getWorld();
                    World playerWorld = playerLoc.getWorld();
                    if (holoWorld == null || playerWorld == null) continue;

                    if (!holoWorld.equals(playerWorld)) {
                        if (isShown) {
                            hologram.hide(player);
                        }
                        continue;
                    } else if (!holoWorld.isChunkLoaded(holoLoc.getBlockX() >> 4, holoLoc.getBlockZ() >> 4) && isShown) {
                        hologram.hide(player);
                        continue;
                    }
                    boolean inRange = holoLoc.distanceSquared(playerLoc) <= this.spawnDistance;

                    if (!inRange && isShown) {
                        hologram.hide(player);
                    } else if (inRange && !isShown) {
                        hologram.show(player);
                    }
                }
            }
        }, 20L, 2L);
    }
}

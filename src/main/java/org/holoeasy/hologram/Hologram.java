package org.holoeasy.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.holoeasy.line.ILine;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Hologram {
    private final Plugin plugin;
    private final IHologramLoader loader;
    private final UUID id = UUID.randomUUID();
    private final List<ILine<?>> hLines = new CopyOnWriteArrayList<>(); // writes are slow and Iterators are fast and consistent.

    private Location location;
    public Hologram(Plugin plugin, Location location, IHologramLoader loader) {
        this.plugin = plugin;
        this.location = location;
        this.loader = loader;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public IHologramLoader getLoader() {
        return loader;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getId() {
        return id;
    }

    public List<ILine<?>> getLines() {
        return hLines;
    }

    private final Set<Player> seeingPlayers = ConcurrentHashMap.newKeySet(); // faster writes

    public Set<Player> getSeeingPlayers() {
        return seeingPlayers;
    }

    @Nullable
    private ShowEvent showEvent = null;
    @Nullable
    private HideEvent hideEvent = null;

    @SuppressWarnings({"unchecked"})
    public <T extends ILine<?>> T lineAt(int index) {
        return (T) hLines.get(index);
    }

    public Hologram onShow(ShowEvent showEvent) {
        this.showEvent = showEvent;
        return this;
    }

    public Hologram onHide(HideEvent hideEvent) {
        this.hideEvent = hideEvent;
        return this;
    }

    public void load(ILine<?>... lines) {
        hLines.clear();

        for (ILine<?> it : lines) {
            it.getPvt().setHologram(this);
        }
        loader.load(this, lines);
    }

    public void teleport(Location to) {
        this.location = to.clone();
        loader.teleport(this);
    }

    public boolean isShownFor(Player player) {
        return seeingPlayers.contains(player);
    }

    public void show(Player player) {
        seeingPlayers.add(player);
        for (ILine<?> line : this.hLines) {
            line.show(player);
        }

        if (showEvent != null) showEvent.onShow(player);
    }

    public void hide(Player player) {
        for (ILine<?> line : this.hLines) {
            line.hide(player);
        }
        seeingPlayers.remove(player);

        if (hideEvent != null) hideEvent.onHide(player);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Hologram)) return false;
        Hologram h = (Hologram) other;
        return id.equals(h.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

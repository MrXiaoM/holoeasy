package org.holoeasy.line;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.holoeasy.animation.Animations;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.reactive.Observer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ILine<T> {

    class PrivateConfig implements Observer {
        ILine<?> line;
        private Hologram hologram;
        @Nullable
        private BukkitTask animationTask = null;
        public PrivateConfig(ILine<?> line) {
            this.line = line;
        }
        public ILine<?> getLine() {
            return line;
        }

        public Hologram getHologram() {
            return hologram;
        }

        public void setHologram(Hologram hologram) {
            this.hologram = hologram;
        }

        public @Nullable BukkitTask getAnimationTask() {
            return animationTask;
        }

        public void setAnimationTask(@Nullable BukkitTask animationTask) {
            this.animationTask = animationTask;
        }

        @Override
        public void observerUpdate() {
            line.update(hologram.getSeeingPlayers());
        }
    }

    Plugin getPlugin();
    Type getType();
    int getEntityId();
    @Nullable Location getLocation();
    T getObj();
    PrivateConfig getPvt();

    void setLocation(Location value);

    void hide(Player player);

    void teleport(Player player);

    void show(Player player);

    void update(Player player);

    default void update(Collection<Player> seeingPlayers) {
        for (Player player : seeingPlayers) {
            update(player);
        }
    }

    default void setAnimation(Animations animation) {
        this.cancelAnimation();
        getPvt().animationTask = animation.task.apply(this);
    }

    default void cancelAnimation() {
        PrivateConfig pvt = getPvt();
        if (pvt.animationTask != null) {
            pvt.animationTask.cancel();
            pvt.animationTask = null;
        }
    }

    enum Type {
        EXTERNAL,

        TEXT_LINE,
        @ApiStatus.Experimental CLICKABLE_TEXT_LINE,

        ITEM_LINE,
        @ApiStatus.Experimental BLOCK_LINE
    }
}
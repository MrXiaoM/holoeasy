package org.holoeasy.pool;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.holoeasy.action.ClickAction;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ClickEvent;
import org.holoeasy.line.ILine;
import org.holoeasy.line.ITextLine;
import org.holoeasy.line.TextLine;
import org.holoeasy.util.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InteractiveHologramPool implements Listener, IHologramPool {
    private final HologramPool pool;
    float minHitDistance;
    float maxHitDistance;
    @Nullable
    ClickAction clickAction;

    public InteractiveHologramPool(
            HologramPool pool,
            float minHitDistance,
            float maxHitDistance,
            @Nullable ClickAction clickAction
    ) {
        if (minHitDistance <= 0) throw new IllegalArgumentException("minHitDistance must be positive");
        if (maxHitDistance >= 120) throw new IllegalArgumentException("maxHitDistance cannot be greater than 120");

        this.pool = pool;
        this.minHitDistance = minHitDistance;
        this.maxHitDistance = maxHitDistance;
        this.clickAction = clickAction;
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    @Nullable
    public ClickAction getClickAction() {
        return clickAction;
    }

    @Override
    public Plugin getPlugin() {
        return pool.getPlugin();
    }

    @Override
    public Hologram get(UUID id) {
        return pool.get(id);
    }


    @Override
    public void takeCareOf(Hologram value) {
        pool.takeCareOf(value);
    }

    @Nullable
    @Override
    public Hologram remove(UUID id) {
        return pool.remove(id);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Player player = e.getPlayer();

            if (clickAction == null) {
                if (!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    return;
                }
            } else {
                switch (clickAction) {
                    case LEFT_CLICK: {
                        if (!e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                            return;
                        }
                        break;
                    }
                    case RIGHT_CLICK: {
                        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                            return;
                        }
                        break;
                    }
                }
            }

            boolean flag = false;
            for (Hologram hologram : pool.holograms.values()) {
                if (flag) break;
                if (!hologram.isShownFor(player)) {
                    continue;
                }
                for (ILine<?> line : hologram.getLines()) {
                    if (flag) break;
                    switch (line.getType()) {
                        case TEXT_LINE: {
                            ITextLine iTextLine = (ITextLine) line;
                            if (!iTextLine.getClickable()) {
                                continue;
                            }

                            TextLine tL = iTextLine.getTextLine();
                            if (tL.getHitbox() == null) {
                                continue;
                            }

                            AABB.Vec3D intersects = tL.getHitbox().intersectsRay(
                                    new AABB.Ray3D(player.getEyeLocation()), minHitDistance, maxHitDistance
                            );
                            if (intersects == null) {
                                continue;
                            }

                            ClickEvent clickEvent = tL.getClickEvent();
                            if (clickEvent != null) clickEvent.onClick(player);
                            flag = true;
                            break;
                        }

                        case EXTERNAL:
                        case CLICKABLE_TEXT_LINE:
                        case ITEM_LINE:
                        case BLOCK_LINE:
                            break;
                    }
                }
            }
        });
    }
}

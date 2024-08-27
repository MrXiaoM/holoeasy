package org.holoeasy;

import org.bukkit.plugin.Plugin;
import org.holoeasy.action.ClickAction;
import org.holoeasy.pool.HologramPool;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.pool.InteractiveHologramPool;

public class HoloEasy {

    public static boolean useLastSupportedVersion = false;

    public static IHologramPool startPool(Plugin plugin, double spawnDistance) {
        return new HologramPool(plugin, spawnDistance);
    }

    public static IHologramPool startInteractivePool(
            Plugin plugin,
            double spawnDistance
    ) {
        return startInteractivePool(plugin, spawnDistance, 0.5f, 5f, null);
    }

    public static IHologramPool startInteractivePool(
            Plugin plugin,
            double spawnDistance,
            float minHitDistance,
            float maxHitDistance
    ) {
        return startInteractivePool(plugin, spawnDistance, minHitDistance, maxHitDistance, null);
    }

    public static IHologramPool startInteractivePool(
            Plugin plugin,
            double spawnDistance,
            ClickAction clickAction
    ) {
        return startInteractivePool(plugin, spawnDistance, 0.5f, 5f, clickAction);
    }

    public static IHologramPool startInteractivePool(
        Plugin plugin,
        double spawnDistance,
        float minHitDistance,
        float maxHitDistance,
        ClickAction clickAction
    ) {
        HologramPool simplepool = new HologramPool(plugin, spawnDistance);
        return new InteractiveHologramPool(
            simplepool,
            minHitDistance,
            maxHitDistance,
            clickAction
        );
    }


}

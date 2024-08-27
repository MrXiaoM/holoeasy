package org.holoeasy.pool;

import org.bukkit.plugin.Plugin;
import org.holoeasy.builder.HologramBuilder;
import org.holoeasy.builder.interfaces.HologramRegisterGroup;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IHologramPool {

    Plugin getPlugin();

    Hologram get(UUID id);

    void takeCareOf(Hologram value);

    @Nullable
    Hologram remove(UUID id);

    default void registerHolograms(HologramRegisterGroup registerGroup) {
        HologramBuilder.registerHolograms(this, registerGroup);
    }

}

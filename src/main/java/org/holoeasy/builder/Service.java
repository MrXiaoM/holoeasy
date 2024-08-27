package org.holoeasy.builder;


import org.bukkit.inventory.ItemStack;
import org.holoeasy.builder.interfaces.HologramConfigGroup;
import org.holoeasy.hologram.TextBlockStandardLoader;
import org.holoeasy.line.*;
import org.holoeasy.reactive.MutableState;
import org.holoeasy.util.Pair;
import org.jetbrains.annotations.Nullable;

public class Service {
    public static final Service INSTANCE = new Service();
    private Service() {}

    public enum RegistrationType {
        PLUGIN,
        POOL
    }

    // might be Plugin as well
    private final ThreadLocal<Pair<RegistrationType, Object>> staticPool = new ThreadLocal<>();
    public ThreadLocal<Pair<RegistrationType, Object>> getStaticPool() {
        return staticPool;
    }

    private final ThreadLocal<HologramConfig> staticHologram = new ThreadLocal<>();
    public ThreadLocal<HologramConfig> getStaticHologram() {
        return staticHologram;
    }

    public Pair<RegistrationType, Object> getStaticRegistration() {
        Pair<RegistrationType, Object> pair = staticPool.get();
        if (pair == null) throw new IllegalStateException("hologram block must be inside a registerHolograms block");
        return pair;
    }


    private HologramConfig getStaticHolo()  {
        HologramConfig holo = staticHologram.get();
        if (holo == null) throw new RuntimeException("You must call config() inside hologram block");
        return holo;
    }

    public void config(HologramConfigGroup configGroup) {
        HologramConfig config = getStaticHolo();
        configGroup.configure(config);
        if (config.loader == null) {
            config.loader = new TextBlockStandardLoader();
        }
    }
    public ITextLine textline(String text) {
        return textline(text, false, null, null, null);
    }

    public ITextLine textline(
            String text, boolean clickable,
            @Nullable Float minHitDistance,
            @Nullable Float maxHitDistance,
            @Nullable Object[] args
    ) {
        HologramConfig holo = getStaticHolo();

        if (minHitDistance == null || maxHitDistance == null) {
            // if (holo.pool == null && clickable) {
            //     throw IllegalStateException("This hologram is not in a pool,so use the method #clickable(text, minHitDistance, maxHitDistance)")
            // }

            TextLine textLine = new TextLine(holo.plugin, text, args, clickable);
            holo.lines.add(textLine);
            return textLine;

        }
        TextLine textLine = new TextLine(holo.plugin, text, args, false);
        ClickableTextLine clickableTextLine = new ClickableTextLine(textLine, minHitDistance, maxHitDistance);
        holo.lines.add(clickableTextLine);
        return clickableTextLine;
    }

    public void itemline(ItemStack block) {
        HologramConfig holo = getStaticHolo();
        ItemLine itemline = new ItemLine(holo.plugin, block);
        holo.lines.add(itemline);
    }

    public void itemlineMutable(MutableState<ItemStack> item) {
        HologramConfig holo = getStaticHolo();
        ItemLine itemline = new ItemLine(holo.plugin, item);
        holo.lines.add(itemline);
    }

    public void blockline(ItemStack block) {
        HologramConfig holo = getStaticHolo();
        BlockLine blockline = new BlockLine(holo.plugin, block);
        holo.lines.add(blockline);
    }

    public void blocklineMutable(MutableState<ItemStack> block) {
        HologramConfig holo = getStaticHolo();
        BlockLine blockline = new BlockLine(holo.plugin, block);
        holo.lines.add(blockline);
    }

    public void customLine(ILine<?> customLine) {
        HologramConfig holo = getStaticHolo();
        holo.lines.add(customLine);
    }
}

package org.holoeasy.line;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.holoeasy.packet.PacketType;
import org.holoeasy.reactive.MutableState;
import org.holoeasy.util.VersionEnum;
import org.holoeasy.util.VersionUtil;
import org.jetbrains.annotations.Nullable;

import static org.holoeasy.ext.send;

public class ItemLine implements ILine<ItemStack> {
    private final Line line;
    private final PacketContainer resetVelocity;
    private final MutableState<ItemStack> _mutableStateOf;
    private ILine.PrivateConfig pvt = new ILine.PrivateConfig(this);
    private boolean firstRender = true;
    public ItemLine(Plugin plugin, MutableState<ItemStack> obj) {
        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            throw new IllegalStateException("This version does not support item lines");
        }
        line = new Line(plugin, EntityType.DROPPED_ITEM);
        resetVelocity = PacketType.VELOCITY().velocity(line.getEntityID(), 0, 0,0);
        _mutableStateOf = obj;
    }
    public ItemLine(Plugin plugin, ItemStack obj) {
        this(plugin, new MutableState<>(obj));
    }


    @Override
    public Plugin getPlugin() {
        return line.getPlugin();
    }

    @Override
    public Type getType() {
        return Type.ITEM_LINE;
    }

    @Override
    public int getEntityId() {
        return line.getEntityID();
    }

    @Override
    public @Nullable Location getLocation() {
        return line.getLocation();
    }

    @Override
    public ItemStack getObj() {
        return _mutableStateOf.get();
    }

    public void setObj(ItemStack item) {
        _mutableStateOf.set(item);
    }

    @Override
    public PrivateConfig getPvt() {
        return null;
    }

    public void setPvt(PrivateConfig pvt) {
        this.pvt = pvt;
    }

    @Override
    public void setLocation(Location value) {
        line.setLocation(value);
    }

    @Override
    public void hide(Player player) {
        line.destroy(player);
    }

    @Override
    public void teleport(Player player) {
        line.teleport(player);
    }

    @Override
    public void show(Player player) {
        line.spawn(player);
        this.update(player);

        send(player, resetVelocity);

        if(firstRender) {
            firstRender = false;
            _mutableStateOf.addObserver(pvt);
        }
    }

    @Override
    public void update(Player player) {
        PacketContainer packet = PacketType.METADATA_ITEM()
                .metadata(getEntityId(), getObj());
        send(player, packet);
    }
}

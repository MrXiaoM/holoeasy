package org.holoeasy.line;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.holoeasy.packet.PacketType;
import org.holoeasy.reactive.MutableState;
import org.jetbrains.annotations.Nullable;

import static org.holoeasy.ext.send;

public class BlockLine implements ILine<ItemStack> {
    Plugin plugin;
    private final Line line;
    private boolean firstRender = true;
    MutableState<ItemStack> _mutableStateOf;
    private PrivateConfig pvt = new ILine.PrivateConfig(this);

    public BlockLine(Plugin plugin, MutableState<ItemStack> obj) {
        this.plugin = plugin;
        this._mutableStateOf = obj;
        this.line = new Line(plugin, EntityType.ARMOR_STAND);
    }

    public BlockLine(Plugin plugin, ItemStack obj) {
        this(plugin, new MutableState<>(obj));
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Type getType() {
        return Type.BLOCK_LINE;
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
        return pvt;
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
        PacketContainer packet = PacketType.METADATA_TEXT()
                .metadata(getEntityId(), null, true);
        send(player, packet);

        this.update(player);

        if (firstRender) {
            firstRender = false;
            _mutableStateOf.addObserver(pvt);
        }
    }

    @Override
    public void update(Player player) {
        PacketContainer packet = PacketType.EQUIPMENT()
                .equip(getEntityId(), _mutableStateOf.get());
        send(player, packet);
    }
}

package org.holoeasy.line;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.holoeasy.packet.PacketType;
import org.holoeasy.reactive.MutableState;
import org.holoeasy.util.AABB;
import org.jetbrains.annotations.Nullable;

import static org.holoeasy.ext.send;

public class TextLine implements ITextLine {
    private final Line line;
    private final Object[] args;
    boolean clickable;
    String obj;
    ClickEvent clickEvent = null;
    private boolean firstRender = true;

    public TextLine(
            Plugin plugin,
            String obj,
            Object[] args,
            boolean clickable
    ) {
        this.args = args;
        this.clickable = clickable;
        line = new Line(plugin, EntityType.ARMOR_STAND);
        if (args == null) {
            this.obj = obj;
        } else {
            this.obj = obj.replace("{}", "%s");
        }
    }

    public TextLine(Plugin plugin, String obj) {
        this(plugin, obj, null, false);
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public boolean getClickable() {
        return clickable;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getObj() {
        return obj;
    }

    private AABB hitbox = null;

    public AABB getHitbox() {
        return hitbox;
    }

    private boolean isEmpty = false;

    @Override
    public TextLine getTextLine() {
        return this;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public String parse(Player player) {
        if (args == null) {
            return obj;
        }
        Object[] res = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object tmp = args[i];
            if (tmp instanceof MutableState) {
                MutableState state = (MutableState) tmp;
                res[i] = state.get();
                if (firstRender) {
                    firstRender = false;
                    state.addObserver(getPvt());
                }
            } else {
                res[i] = tmp;
            }
        }

        return String.format(obj, res);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public Plugin getPlugin() {
        return line.getPlugin();
    }

    @Override
    public Type getType() {
        return Type.TEXT_LINE;
    }

    @Override
    public int getEntityId() {
        return line.getEntityID();
    }

    @Override
    public @Nullable Location getLocation() {
        return line.getLocation();
    }

    ILine.PrivateConfig pvt = new ILine.PrivateConfig(this);

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
        if (clickable) {
            double chars = obj.length();
            double size = 0.105;
            double dist = size * (chars / 2.0);

            AABB aabb = new AABB(
                    new AABB.Vec3D(-dist, -0.040, -dist),
                    new AABB.Vec3D(dist, +0.040, dist)
            );
            aabb.translate(AABB.Vec3D.fromLocation(value.clone().add(0.0, 2.35, 0.0)));
            hitbox = aabb;
        }
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
        isEmpty = obj.isEmpty();
        if (!isEmpty) {
            line.spawn(player);
            PacketContainer packet = PacketType.METADATA_TEXT().metadata(getEntityId(), parse(player));
            send(player, packet);
        }
    }

    @Override
    public void update(Player player) {
        int spawnBefore = ((isEmpty ? 1 : 0) | ((obj.isEmpty() ? 1 : 0) << 1));
        /*  0x00  = is already showed
            0x01  = is hided but now has changed
            0x02  = is already showed but is empty
            0x03  = is hided and isn't changed      */
        switch (spawnBefore) {
            case 0x03:
                break;
            case 0x02: {
                line.destroy(player);
                isEmpty = true;
                break;
            }

            case 0x01: {
                line.spawn(player);
                isEmpty = false;
                PacketContainer packet = PacketType.METADATA_TEXT()
                        .metadata(getEntityId(), parse(player));
                send(player, packet);
                break;
            }

            case 0x00: {
                PacketContainer packet = PacketType.METADATA_TEXT()
                        .metadata(getEntityId(), parse(player), false);
                send(player, packet);
                break;
            }
        }
    }
}

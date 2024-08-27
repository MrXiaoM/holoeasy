package org.holoeasy.line;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.holoeasy.util.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static org.holoeasy.util.AABB.Vec3D.fromLocation;

public class ClickableTextLine implements Listener, ITextLine {
    private final float minHitDistance;
    private final float maxHitDistance;
    private final TextLine line;
    private AABB hitbox;
    private final Set<Integer> playersClickable = new HashSet<>();
    private ILine.PrivateConfig pvt = new ILine.PrivateConfig(this);

    public ClickableTextLine(TextLine line, float minHitDistance, float maxHitDistance) {
        if (minHitDistance <= 0) throw new IllegalArgumentException("minHitDistance must be positive");
        if (maxHitDistance > 120) throw new IllegalArgumentException("maxHitDistance cannot be greater than 120");
        this.minHitDistance = minHitDistance;
        this.maxHitDistance = maxHitDistance;
        this.line = line;

        if (line.getLocation() != null) {
            this.updateHitBox();
        }

        Bukkit.getPluginManager().registerEvents(this, line.getPlugin());
    }

    @Override
    public boolean getClickable() {
        return false;
    }

    @Override
    public TextLine getTextLine() {
        return line;
    }

    @Override
    public Object[] getArgs() {
        return getTextLine().getArgs();
    }

    @Override
    public String parse(Player player) {
        return line.parse(player);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        line.onClick(clickEvent);
    }

    @Override
    public Plugin getPlugin() {
        return line.getPlugin();
    }

    @Override
    public Type getType() {
        return Type.CLICKABLE_TEXT_LINE;
    }

    @Override
    public int getEntityId() {
        return line.getEntityId();
    }

    @Override
    public @Nullable Location getLocation() {
        return line.getLocation();
    }

    @Override
    public String getObj() {
        return line.obj;
    }

    public void setObj(String value) {
        line.obj = value;
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
        this.updateHitBox();
    }

    @Override
    public void hide(Player player) {
        line.hide(player);

        playersClickable.remove(player.getEntityId());
    }

    @Override
    public void teleport(Player player) {
        line.teleport(player);
    }

    @Override
    public void show(Player player) {
        line.show(player);

        playersClickable.add(player.getEntityId());
    }

    @Override
    public void update(Player player) {
        line.update(player);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            return;
        }
        if (hitbox == null) {
            return;
        }

        if (!playersClickable.contains(player.getEntityId())) {
            return;
        }

        AABB.Vec3D ray = hitbox.intersectsRay(new AABB.Ray3D(player.getEyeLocation()), minHitDistance, maxHitDistance);
        if (ray == null) return;
        if (line.clickEvent != null) {
            line.clickEvent.onClick(player);
        }
    }

    private void updateHitBox() {
        double chars = getObj().length();
        double size = 0.105;
        double dist = size * (chars / 2.0);

        Location location = getLocation();
        if (location != null) {
            AABB aabb = new AABB(
                    new AABB.Vec3D(-dist, -0.040, -dist),
                    new AABB.Vec3D(dist, +0.040, dist)
            );
            aabb.translate(fromLocation(location.clone().add(0.0, 2.35, 0.0)));
            hitbox = aabb;
        }
    }
}

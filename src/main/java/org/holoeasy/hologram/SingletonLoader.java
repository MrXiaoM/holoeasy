package org.holoeasy.hologram;

import org.bukkit.Location;
import org.holoeasy.line.ILine;

public class SingletonLoader implements IHologramLoader {
    @Override
    public void load(Hologram hologram, ILine<?>[] lines) {
        if (lines.length > 1) {
            throw new RuntimeException("Hologram '" + hologram.getId() + "' has more than 1 line.");
        }

        Location cloned = hologram.getLocation().clone();

        ILine<?> line = lines[0];

        line.setLocation(cloned);
        hologram.getLines().add(line);
    }

    @Override
    public void teleport(Hologram hologram) {
        ILine<?> line = hologram.getLines().get(0);

        line.setLocation(hologram.getLocation().clone());
        hologram.getSeeingPlayers().forEach(line::teleport);
    }
}

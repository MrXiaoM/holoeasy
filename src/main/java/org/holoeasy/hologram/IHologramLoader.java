package org.holoeasy.hologram;

import org.holoeasy.line.ILine;


public interface IHologramLoader {
    void load(Hologram hologram, ILine<?>[] lines);
    void teleport(Hologram hologram);
}
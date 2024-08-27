package org.holoeasy.hologram;


import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.holoeasy.line.ILine;
import org.holoeasy.line.ITextLine;
import org.holoeasy.line.TextLine;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Experimental
public class TextSequentialLoader implements IHologramLoader {
    @Override
    public void load(Hologram hologram, ILine<?>[] lines) {
        set(hologram, Lists.newArrayList(lines), true);
    }

    @Override
    public void teleport(Hologram hologram) {
        set(hologram, hologram.getLines(), false);
        // TODO: When teleporting, the holograms unexpectedly become distant. Understand why.
    }

    private void set(Hologram hologram, List<ILine<?>> lines, boolean add) {
        Location cloned = hologram.getLocation().clone();
        for (ILine<?> line : lines) {
            switch (line.getType()) {
                case TEXT_LINE:
                case CLICKABLE_TEXT_LINE: {
                    TextLine tL = ((ITextLine) line).getTextLine();

                    // add to lines
                    tL.setLocation(cloned.clone());

                    if (add) {
                        hologram.getLines().add(0, tL);
                    } else {
                        for (Player it : hologram.getSeeingPlayers()) {
                            tL.teleport(it);
                        }
                    }
                    cloned.setZ(cloned.getZ() + 0.175 * tL.getObj().length());
                    break;
                }

                default:
                    throw new RuntimeException("This method load supports only TextLine & TextALine & ClickableTextLine.");
            }
        }
    }
}

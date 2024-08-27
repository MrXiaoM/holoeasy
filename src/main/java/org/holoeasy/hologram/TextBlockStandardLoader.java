package org.holoeasy.hologram;

import org.bukkit.Location;
import org.holoeasy.line.ILine;

import java.util.List;

import static java.lang.Math.abs;

public class TextBlockStandardLoader implements IHologramLoader {

    public final double LINE_HEIGHT = 0.28;

    @Override
    public void load(Hologram hologram, ILine<?>[] lines) {
        Location hologramLocation = hologram.getLocation().clone();

        if (lines.length == 1) {
            ILine<?> line = lines[0];

            line.setLocation(hologramLocation);
            hologram.getLines().add(line);
            return;
        }

        // reverse A - B - C to C - B - A
        reverse(lines);

        hologramLocation.subtract(0.0, LINE_HEIGHT, 0.0);

        for (int j = 0; j < lines.length; j++) {
            ILine<?> line = lines[j];

            double up = LINE_HEIGHT;

            if (j > 0) {
                switch (lines[j - 1].getType()) {
                    case ITEM_LINE:
                        up = -1.5;
                        break;
                    case BLOCK_LINE:
                        up = -0.19;
                        break;
                    case EXTERNAL:
                    case TEXT_LINE:
                    case CLICKABLE_TEXT_LINE:
                        break;
                }
            }

            switch (line.getType()) {
                case TEXT_LINE:
                case CLICKABLE_TEXT_LINE:
                case BLOCK_LINE: {
                    line.setLocation(hologramLocation.add(0.0, up, 0.0).clone());
                    hologram.getLines().add(0, line);
                    break;
                }

                case ITEM_LINE: {
                    line.setLocation(hologramLocation.add(0.0, 0.6, 0.0).clone());
                    hologram.getLines().add(0, line);
                    break;
                }


                default:
                    throw new RuntimeException("This method load does not support line type " + line.getType().name());
            }
        }
    }

    @Override
    public void teleport(Hologram hologram) {
        List<ILine<?>> lines = hologram.getLines();
        ILine<?> firstLine = lines.get(0);
        // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
        Location location = firstLine.getLocation();
        if (location == null) throw new RuntimeException("First line has not a location");
        double baseY = location.getY();
        // Get position Y where to teleport the first line
        double destY = (hologram.getLocation().getY() - LINE_HEIGHT);

        switch (firstLine.getType()) {
            case TEXT_LINE:
            case CLICKABLE_TEXT_LINE:
            case BLOCK_LINE:
                destY += LINE_HEIGHT;
                break;
            default:
                destY += 0.6;
                break;
        }

        // Teleport the first line
        this.teleportLine(hologram, destY, firstLine);
        ILine<?> tempLine;
        for (int j = 1; j < lines.size(); j++) {
            tempLine = lines.get(j);
            Location loc = tempLine.getLocation();
            if (loc == null) {
                throw new RuntimeException("Missing location of line " + tempLine);
            }
            /*
            Teleport from the second line onwards.
            The final height is found by adding to that of the first line the difference that was present when it was already spawned
            */
            this.teleportLine(hologram, destY + abs(baseY - loc.getY()), tempLine);
        }
    }

    private void teleportLine(Hologram hologram, double destY, ILine<?> tempLine) {
        Location dest = hologram.getLocation().clone();
        dest.setY(destY);
        tempLine.setLocation(dest);
        hologram.getSeeingPlayers().forEach(tempLine::teleport);
    }

    public static <T> void reverse(T[] array) {
        int i = 0, j = array.length - 1;
        while (i < j) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
    }
}
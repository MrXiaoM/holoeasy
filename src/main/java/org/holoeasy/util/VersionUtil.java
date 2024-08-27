/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.holoeasy.util;

import org.bukkit.Bukkit;

public class VersionUtil {

    public static VersionEnum CLEAN_VERSION;

    static {
        // Bukkit method that was added in 2011
        // Example value: 1.20.4-R0.1-SNAPSHOT
        // Example value: 1.14-R0.1-SNAPSHOT

        String bkName = Bukkit.getServer().getBukkitVersion();

        String bkVersion = bkName.split("-")[0];

        // 1.20.4
        // 1.14
        // Split with '.' and get first two elements

        String[] split = bkVersion.split("\\.");
        String version = "V" + split[0] + "_" + split[1];

        try {
            CLEAN_VERSION = VersionEnum.valueOf(version);
        } catch (IllegalArgumentException ignored) {
            CLEAN_VERSION = VersionEnum.LATEST;
        }
    }

    public static boolean isCompatible(VersionEnum ve) {
        return CLEAN_VERSION.equals(ve);
    }

    public static boolean isAbove(VersionEnum ve) {
        return CLEAN_VERSION.ordinal() >= ve.ordinal();
    }

    public static boolean isBelow(VersionEnum ve) {
        return CLEAN_VERSION.ordinal() <= ve.ordinal();
    }

    public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }
}

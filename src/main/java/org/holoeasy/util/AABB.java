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

import org.bukkit.Location;
import org.bukkit.util.Vector;
import java.util.*;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.sqrt;

/**
 * Represents an axix-aligned bounding box.
 *
 * @author Kristian
 * @author unldenis
 */
public class AABB {
    private Vec3D min;
    private Vec3D max;
    /**
    * Creates a new instance from a minimum point and a maximum point.
    */
    public AABB(Vec3D min, Vec3D max) {
        this.min = min;
        this.max = max;
    }
    /**
     * Create a new com.github.unldenis.hologram.util.AABB from a given block.
     *
     * @param block - the block.
     */
    public AABB(Location block) {
        this(Vec3D.fromLocation(block), Vec3D.fromLocation(block).add(Vec3D.UNIT_MAX));
    }

    /**
     * Translate this AABB to a given point.
     *
     * @param vec - the point.
     */
    public void translate(Vec3D vec) {
        this.min = min.add(vec);
        this.max = max.add(vec);
    }

    /**
     * Calculates intersection with the given ray between a certain distance interval.
     *
     *
     * Ray-box intersection is using IEEE numerical properties to ensure the test is both robust and
     * efficient, as described in:
     * <br></br>
     * `Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
     * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics tools, 10(1):49-54,
     * 2005`
     *
     * @param ray incident ray
     * @return intersection point on the bounding box (only the first is returned) or null if no
     * intersection
     */
    @Nullable
    public Vec3D intersectsRay(Ray3D ray, float minDist, float maxDist) {
        Vec3D invDir = new Vec3D(1f / ray.direction.x, 1f / ray.direction.y, 1f / ray.direction.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vec3D bbox = signDirX ? max : min;
        double tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - ray.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        double tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - ray.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public static class Vec3D {
        /**
         * X coordinate.
         */
        double x;

        /**
         * Y coordinate.
         */
        double y;

        /**
         * Z coordinate.
         */
        double z;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        /**
         * Creates a new vector with the given coordinates.
         *
         * @param x the x
         * @param y the y
         * @param z the z
         */
        public Vec3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Creates a new vector with the coordinates of the given vector.
         *
         * @param v vector to copy.
         */
        public Vec3D(Vec3D v) {
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
        }

        /**
         * Add vector v and returns result as new vector.
         *
         * @param v vector to add
         * @return result as new vector
         */
        public Vec3D add(Vec3D v) {
            return new Vec3D(x + v.x, y + v.y, z + v.z);
        }

        /**
         * Scales vector uniformly and returns result as new vector.
         *
         * @param s scale factor
         * @return new vector
         */
        public Vec3D scale(Double s) {
            return new Vec3D(x * s, y * s, z * s);
        }

        /**
         * Normalizes the vector so that its magnitude = 1.
         *
         * @return The normalized vector.
         */
        public Vec3D normalize() {
            double mag = sqrt(x * x + y * y + z * z);

            if (mag > 0) {
                return scale(1.0 / mag);
            }
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Vec3D) {
                Vec3D v = (Vec3D) obj;
                return x == v.x && y == v.y && z == v.z;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return String.format("{x: %g, y: %g, z: %g}", x, y, z);
        }

        /**
         * Point with the coordinate (1, 1, 1).
         */
        public static final Vec3D UNIT_MAX = new Vec3D(1.0, 1.0, 1.0);

        /**
         * Construct a vector from a Bukkit location.
         *
         * @param loc - the Bukkit location.
         */

        public static Vec3D fromLocation(Location loc) {
            return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
        }

        /**
         * Construct a copy of our immutable vector from Bukkit's mutable vector.
         *
         * @param v - Bukkit vector.
         * @return A copy of the given vector.
         */
        public static Vec3D fromVector(Vector v) {
            return new Vec3D(v.getX(), v.getY(), v.getZ());
        }
    }

    public static class Ray3D extends Vec3D {
        Vec3D origin;
        Vec3D direction;
        public Ray3D(Vec3D origin, Vec3D direction){
            super(origin);
            this.origin = origin;
            this.direction = direction.normalize();
        }
        /**
         * Construct a 3D ray from a location.
         *
         * @param loc - the Bukkit location.
         */
        public Ray3D(Location loc) {
            this(fromLocation(loc), fromVector(loc.getDirection()));
        }

        public Vec3D getDirection() {
            return direction;
        }

        public Vec3D getPointAtDistance(double dist) {
            return add(direction.scale(dist));
        }

        public String toString() {
            return "origin: " + super.toString() + " dir: " + direction;
        }
    }
}
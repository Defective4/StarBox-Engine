package net.defekt.minecraft.starbox.world;

import java.util.Objects;

public class Location {
    private final double x, y, z;
    private final float yaw, pitch;

    public Location(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public int getBlockX() {
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Location toChunkLocation() {
        return new Location(Math.floorDiv(getBlockX(), 16), getBlockY(), Math.floorDiv(getBlockZ(), 16));
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0 && Double.compare(location.z,
                                                                                                          z) == 0 && Float.compare(
                location.yaw,
                yaw) == 0 && Float.compare(location.pitch, pitch) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }
}

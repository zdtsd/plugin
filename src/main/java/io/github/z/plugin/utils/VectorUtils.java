package io.github.z.plugin.utils;

import org.bukkit.util.Vector;

public class VectorUtils {

    public static Vector clampSlope(Vector vec, double minSlope, double maxSlope){
        return clampSlopeMin(clampSlopeMax(vec, maxSlope), minSlope);
    }
    public static Vector clampSlopeMin(Vector vec, double slope){
        double length = vec.length();
        double x = vec.getX();
        double y = vec.getY();
        double z = vec.getZ();
        double xz = Math.sqrt(x * x + z * z);
        if(y / xz < slope){
            y = xz * slope;
        }
        return new Vector(x, y, z).normalize().multiply(length);
    }

    public static Vector clampSlopeMax(Vector vec, double slope){
        double length = vec.length();
        double x = vec.getX();
        double y = vec.getY();
        double z = vec.getZ();
        double xz = Math.sqrt(x * x + z * z);
        if(y / xz > slope){
            y = xz * slope;
        }
        return new Vector(x, y, z).normalize().multiply(length);
    }
}

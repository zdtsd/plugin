package io.github.z.plugin.utils;

import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class LocationUtils {
    public static boolean collidesWithBlocks(BoundingBox boundingBox, World world) {
        return collidesWithBlocks(boundingBox, world, true);
    }
    public static boolean collidesWithBlocks(BoundingBox boundingBox, World world, boolean loadChunks) {
        //TODO: Load chunks if needed
        return world.hasCollisionsIn(boundingBox);
    }

    public static boolean travelTillObstructed(World world, BoundingBox movingBoundingBox, double maxDistance,
            Vector vector, double increment, boolean wiggleY
    ) {
        Vector start = movingBoundingBox.getCenter();
        Vector vectorIncrement = vector.clone().normalize().multiply(increment);

        // this box always moves along a straight line, even if wiggle room is enabled
        BoundingBox testBox = movingBoundingBox.clone();

        double maxIterations = maxDistance / increment + 1;
        for (int i = 0; i < maxIterations; i++) {
            testBox.shift(vectorIncrement);
            Vector testBoxCentre = testBox.getCenter();

            if (!testBox.getMin().toLocation(world).isChunkLoaded() || !testBox.getMax().toLocation(world).isChunkLoaded()) {
                return true;
            }

            if (start.distanceSquared(testBoxCentre) > maxDistance * maxDistance) {
                return false;
            }

            double wiggleFactor = 2.0;
            if (collidesWithBlocks(testBox, world, false)) {
                if (wiggleY) {
                    boolean blocked = true;
                    BoundingBox wiggleBox = testBox.clone();
                    double step = 0.1;
                    int steps = (int) (wiggleBox.getHeight() * wiggleFactor / step);
                    wiggleBox.shift(0, -wiggleBox.getHeight() / 2 + step / 2, 0);
                    for (int dy = 0; dy < steps; dy++) {
                        // Scan along the y-axis, from -height/2+step/2 to +height/2-step/2, to find the lowest available space.
                        if (!collidesWithBlocks(wiggleBox, world, false)) {
                            blocked = false;
                            break;
                        }
                        wiggleBox.shift(0, step, 0);
                    }
                    if (blocked) {
                        return true;
                    }
                    movingBoundingBox.copy(wiggleBox);
                } else {
                    return true;
                }
            } else {
                movingBoundingBox.copy(testBox);
            }
        }
        return false;
    }
}

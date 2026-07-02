package io.github.z.plugin.utils;

import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public class LocationUtils {
    public static boolean collidesWithBlocks(BoundingBox boundingBox, World world) {
        return collidesWithBlocks(boundingBox, world, true);
    }
    public static boolean collidesWithBlocks(BoundingBox boundingBox, World world, boolean loadChunks) {
        //TODO: Load chunks if needed
        return world.hasCollisionsIn(boundingBox);
    }
}

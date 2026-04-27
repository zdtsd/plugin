package io.github.z.plugin.utils;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Set;

public class BlockUtils {

    private static final Set<Material> INDESTRUCTIBLE = Set.of(
            Material.AIR,
            Material.CAVE_AIR,
            Material.VOID_AIR,
            Material.BARRIER,
            Material.STRUCTURE_VOID,
            Material.BEDROCK,
            Material.OBSIDIAN,
            Material.LIGHT
    );

    /**
     * Naturally breaks all blocks in a cylinder around center, extending upward for height layers.
     */
    public static void breakCylinder(Location center, int radius, int height, boolean breakNaturally) {
        for (int dy = 0; dy < height; dy++) {
            breakCircle(center.clone().add(0, dy, 0), radius, breakNaturally);
        }
    }

    /**
     * Replaces all non-indestructible blocks in a cylinder around center with the specified material.
     */
    public static void replaceCylinder(Location center, int radius, int height, Material material, boolean breakNaturally) {
        for (int dy = 0; dy < height; dy++) {
            replaceCircle(center.clone().add(0, dy, 0), radius, material, breakNaturally);
        }
    }

    /**
     * Replaces all non-indestructible blocks in a flat (Y-level) circle around center with the specified material.
     */
    public static void replaceCircle(Location center, int radius, Material material, boolean breakNaturally) {
        int radiusSq = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radiusSq) {
                    replaceBlock(center.clone().add(dx, 0, dz), material, breakNaturally);
                }
            }
        }
    }

    /**
     * Naturally breaks all blocks in a flat (Y-level) circle around center.
     */
    public static void breakCircle(Location center, int radius, boolean breakNaturally) {
        int radiusSq = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radiusSq) {
                    breakBlock(center.clone().add(dx, 0, dz), breakNaturally);
                }
            }
        }
    }

    /**
     * Breaks the block at the given location and replaces it with the specified material,
     * unless the block is indestructible. Drops loot if breakNaturally is true.
     *
     * @return true if the block was replaced, false if it was protected
     */
    public static boolean replaceBlock(Location location, Material material, boolean breakNaturally) {
        if (INDESTRUCTIBLE.contains(location.getBlock().getType())) {
            return false;
        }
        if (breakNaturally) {
            location.getBlock().breakNaturally();
        }
        location.getBlock().setType(material);
        return true;
    }

    /**
     * Breaks the block at the given location, unless it is indestructible.
     * Drops loot if breakNaturally is true, otherwise removes it silently.
     *
     * @return true if the block was broken, false if it was protected
     */
    public static boolean breakBlock(Location location, boolean breakNaturally) {
        if (INDESTRUCTIBLE.contains(location.getBlock().getType())) {
            return false;
        }
        if (breakNaturally) {
            location.getBlock().breakNaturally();
        } else {
            location.getBlock().setType(Material.AIR);
        }
        return true;
    }
}

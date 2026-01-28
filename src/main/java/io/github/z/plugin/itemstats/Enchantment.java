package io.github.z.plugin.itemstats;

import org.bukkit.inventory.ItemStack;

public interface Enchantment extends ItemStat {
    //TODO: Add enchantment stuff
    public default void onApply(ItemStack item, int level){
        return;
    }
}

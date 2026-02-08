package io.github.z.plugin.itemstats;

import io.github.z.plugin.GenericPlayerModifier;
import io.github.z.plugin.events.DamageEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface ItemStat extends GenericPlayerModifier {
    /*
    Returns the default value of the stat (usually 0 for additive, 1 for multiplicative)
    */
    default double getDefaultValue() {
        return 0;
    }


    //Used to mask real stats with dummy stats, runs only when the item is updated
    default void onUpdate(ItemStack item, double level){
        return;
    }

}

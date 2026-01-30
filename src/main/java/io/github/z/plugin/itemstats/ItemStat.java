package io.github.z.plugin.itemstats;

import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface ItemStat {

    /*
    Return the name of the ItemStat.
     */
    String getName();

    /*
    Returns the default value of the stat (usually 0 for additive, 1 for multiplicative)
     */
    default double getDefaultValue() {
        return 0;
    }

    /*
    Returns the priority order for event handling, with lower values handled first.
    Stats which must run first are around -1000.
    Default is 1000.
    Base stats are -100.
    Stats with a specific order are 0-100.
    Stats which read final damage values are around 2000
    Stats which must run last are around 10000.
     */
    default double getPriorityAmount() {
        return 1000;
    }

    /*
    Runs every 5 ticks.
    @param plugin: the main Plugin.
	@param player: the Player running the action
	@param value: the value of ItemStat possessed by the Player
	@param twoHz: true every 10 ticks
	@param oneHz: true every 20 ticks
     */

    default void tick(Plugin plugin, Player player, double value, boolean twoHz, boolean oneHz){

    }

    default void onDamage(Player player, DamageEvent event, double level){

    }
    default void onHurt(Player player, DamageEvent event, double level){

    };



    //Used to mask real stats with dummy stats, runs only when the item is updated
    default void onUpdate(ItemStack item, double level){
        return;
    }

}

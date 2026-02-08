package io.github.z.plugin;

import io.github.z.plugin.events.DamageEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface GenericPlayerModifier {

    /*
   Return the name of the modifier.
    */
    String getName();


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

    }

    default void onProjectileLaunch(Player player, ProjectileLaunchEvent event, double level){

    }

    default void onBowShoot(Player player, EntityShootBowEvent event, double level){

    }

    default void onCrossbowShoot(Player player, EntityShootBowEvent event, double level){

    }

    default void onCrossbowLoad(Player player, EntityLoadCrossbowEvent event, double level){

    }
}

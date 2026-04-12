package io.github.z.plugin;

import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public interface GenericEntityModifier {


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
    default void tick(Entity entity, double value, boolean twoHz, boolean oneHz){

    }

    default void onDamage(Entity entity, DamageEvent event, double level){

    }
    default void onHurt(Entity entity, DamageEvent event, double level){

    }

    default void onApplyEffect(Entity entity, ApplyEffectEvent event, double level){

    }
    default void onReceiveEffect(Entity entity, ApplyEffectEvent event, double level){

    }

    default void onProjectileLaunch(Entity entity, ProjectileLaunchEvent event, double level){

    }

    default void onBowShoot(Entity entity, EntityShootBowEvent event, double level){

    }

    default void onCrossbowShoot(Entity entity, EntityShootBowEvent event, double level){

    }

    default void onCrossbowLoad(Entity entity, EntityLoadCrossbowEvent event, double level){

    }

    default void onPlayerLandsOnGround(Entity entity, PlayerLandsOnGroundEvent event, double level){

    }
}

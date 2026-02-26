package io.github.z.plugin;

import io.github.z.plugin.events.DamageEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public interface GenericEntityModifier {

    default void tick(Entity entity, double value, boolean twoHz, boolean oneHz){

    }

    default void onDamage(Entity entity, DamageEvent event, double level){

    }
    default void onHurt(Entity entity, DamageEvent event, double level){

    }

    default void onProjectileLaunch(Entity entity, ProjectileLaunchEvent event, double level){

    }

    default void onBowShoot(Entity entity, EntityShootBowEvent event, double level){

    }

    default void onCrossbowShoot(Entity entity, EntityShootBowEvent event, double level){

    }

    default void onCrossbowLoad(Entity entity, EntityLoadCrossbowEvent event, double level){

    }
}

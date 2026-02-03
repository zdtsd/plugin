package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.ProjectileUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class BaseProjectileSpeed implements Attribute {
    @Override
    public String getName() {
        return "proj_speed_base";
    }
    @Override
    public double getPriorityAmount() {
        return -100;
    }

    @Override
    public double getDefaultValue() {
        return 1;
    }


    @Override
    public void onProjectileLaunch(Player player, ProjectileLaunchEvent event, double level){
        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(level));
    }
}

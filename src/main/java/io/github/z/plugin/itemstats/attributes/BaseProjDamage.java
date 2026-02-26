package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.ProjectileUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class BaseProjDamage implements Attribute {
    @Override
    public String getName() {
        return "proj_damage_base";
    }

    @Override
    public double getPriorityAmount() {
        return -100;
    }

    @Override
    public void onProjectileLaunch(Entity entity, ProjectileLaunchEvent event, double level){
        ProjectileUtils.setDamage(event.getEntity(), level);
    }
}

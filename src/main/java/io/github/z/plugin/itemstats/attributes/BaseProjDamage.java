package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.PlayerUtils;
import io.github.z.plugin.utils.ProjectileUtils;
import io.github.z.plugin.utils.ScoreboardUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
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
    public void onProjectileLaunch(Player player, ProjectileLaunchEvent event, double level){
        ProjectileUtils.setDamage(event.getEntity(), level);
    }
}

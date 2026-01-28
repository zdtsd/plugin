package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PointBlank implements Enchantment {

    private static final double maxRange = 8;
    private static final double boostPerLevel = 1;
    @Override
    public String getName() {
        return "point_blank";
    }

    @Override
    public void onHurt(Player player, DamageEvent event, double level){
        if(event.isProjectile() && player.getLocation().distance(event.getDamagee().getLocation()) <= maxRange){
            event.addBaseDamage(level * boostPerLevel);
        }
    }
}

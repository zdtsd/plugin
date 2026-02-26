package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class LogDamage implements Enchantment {
    @Override
    public String getName() {
        return "LogDamage";
    }

    @Override
    public void onDamage(Entity entity, DamageEvent event, double level){
        Bukkit.getLogger().info("Player " + entity.getName() + " dealt " + event.getDamage() + " damage.");
    }

    @Override
    public double getPriorityAmount(){
        return 10000;
    }
}

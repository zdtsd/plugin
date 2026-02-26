package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class TestingOne implements Enchantment {

    @Override
    public String getName() {
        return "Testing1";
    }

    @Override
    public void tick(Entity entity, double value, boolean twoHz, boolean oneHz){
        Bukkit.getLogger().info(getName() + ": " + value);
    }
}

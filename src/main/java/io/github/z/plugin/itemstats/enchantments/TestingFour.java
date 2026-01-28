package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.itemstats.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TestingFour implements Enchantment {

    @Override
    public String getName() {
        return "Testing4";
    }


    @Override
    public void tick(Plugin plugin, Player player, double value, boolean twoHz, boolean oneHz){
        Bukkit.getLogger().info(getName() + ": " + value);
    }
}

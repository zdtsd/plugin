package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.itemstats.Enchantment;
import io.github.z.plugin.itemstats.ItemStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TestingOne implements Enchantment {

    @Override
    public String getName() {
        return "Testing1";
    }

    @Override
    public void tick(Plugin plugin, Player player, double value, boolean twoHz, boolean oneHz){
        Bukkit.getLogger().info(getName() + ": " + value);
    }
}

package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.utils.ShieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BaseShieldRegen implements Attribute {

    @Override
    public String getName() {
        return "shield_regen_base";
    }


    @Override
    public void tick(Entity entity, double value, boolean twoHz, boolean oneHz) {
        if(entity instanceof Player player){
            if(player.getActiveItem().getType() != Material.SHIELD){
                Bukkit.getLogger().info("Healing!");
                double healValue = value / 4;
                ShieldUtils.healShield(player, healValue);
            }
            else {
                Bukkit.getLogger().info("NOT healing!");
            }
        }
    }
}

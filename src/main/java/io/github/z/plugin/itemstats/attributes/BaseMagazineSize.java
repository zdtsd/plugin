package io.github.z.plugin.itemstats.attributes;

import io.github.z.plugin.itemstats.Attribute;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.utils.ProjectileUtils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

public class BaseMagazineSize implements Attribute {


    private static final int cooldown = 10;
    @Override
    public String getName() {
        return "magazine_size_base";
    }

    @Override
    public double getPriorityAmount() {
        return -100;
    }

    @Override
    public void onCrossbowLoad(Player player, EntityLoadCrossbowEvent event, double level) {
        ItemStack crossbow = event.getCrossbow();
        ItemStatUtils.setItemData(crossbow, ProjectileUtils.crossbowAmmoNBTTag, level);
    }

    @Override
    public void onCrossbowShoot(Player player, EntityShootBowEvent event, double level) {
        ItemStack crossbow = event.getBow();
        if(crossbow.getItemMeta() instanceof CrossbowMeta meta){
            double ammoCount = ItemStatUtils.getItemData(crossbow, ProjectileUtils.crossbowAmmoNBTTag) - 1;
            if(ammoCount > 0){
                ItemStack arrow = new ItemStack(Material.ARROW);
                meta.addChargedProjectile(arrow);
                crossbow.setItemMeta(meta);
                player.setCooldown(Material.CROSSBOW, cooldown);
                ItemStatUtils.setItemData(crossbow, ProjectileUtils.crossbowAmmoNBTTag, ammoCount);
            }
        }

    }
}

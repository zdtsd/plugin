package io.github.z.plugin.listeners;

import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.utils.ProjectileUtils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerProjLaunchEvent(ProjectileLaunchEvent event){
        if(event.getEntity().getShooter() instanceof Player player){
            ItemStatManager.onProjectileLaunch(player, event);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerShootBowEvent(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player player){
            ProjectileUtils.setForce((Projectile) event.getProjectile(), event.getForce());
            ItemStatManager.onBowShoot(player, event);

            //Handle crossbow shot events
            ItemStack bowShot = event.getBow();
            if(bowShot != null && bowShot.getType() == Material.CROSSBOW){
                ItemStatManager.onCrossbowShoot(player, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerLoadCrossbowEvent(EntityLoadCrossbowEvent event){
        if(event.getEntity() instanceof Player player){
            ItemStatManager.onCrossbowLoad(player, event);
        }
    }
}

package io.github.z.plugin.listeners;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.utils.ProjectileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void entityDamageEvent(EntityDamageEvent event){
        if(event.getEntity() instanceof LivingEntity le){
            Bukkit.getPluginManager().callEvent(new DamageEvent(event, le));
        }

        if(event.getDamage() < 0){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void customDamageEvent(DamageEvent event){
        LivingEntity damagee = event.getDamagee();
        Entity damager = event.getDamager();
        Entity source = event.getSource();

        //Set base projectile damage from scoreboard tag.
        if(damager instanceof Projectile proj){
            event.setBaseDamage(ProjectileUtils.getDamage(proj) * ProjectileUtils.getForce(proj));
        }

        if(source instanceof Player player){
            ItemStatManager.onDamage(player, event);
        }
        if(damagee instanceof Player player){
            ItemStatManager.onHurt(player, event);
        }
    }
}

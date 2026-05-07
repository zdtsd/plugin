package io.github.z.plugin.listeners;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.mobspells.MobSpellManager;
import io.github.z.plugin.utils.DamageUtils;
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
            DamageEvent.Metadata metadata = DamageUtils.nextMetadata;
            DamageUtils.nextMetadata = null;
            DamageEvent damageEvent = metadata != null
                    ? new DamageEvent(event, le, metadata)
                    : new DamageEvent(event, le);
            Bukkit.getPluginManager().callEvent(damageEvent);
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
            AbilityManager.onDamage(player, event);
        } else if(source instanceof LivingEntity le){
            MobSpellManager.onDamage(le, event);
        }
        if(damagee instanceof Player player){
            ItemStatManager.onHurt(player, event);
            AbilityManager.onHurt(player, event);
        } else if(damagee instanceof LivingEntity){
            MobSpellManager.onHurt(damagee, event);
        }
        if(source != null){
            EffectManager.onDamage(source, event);
        }
        if(damagee != null){
            EffectManager.onHurt(damagee, event);
        }

        if(!event.isCancelled() && event.getDamage() >= damagee.getHealth()){
            if(source instanceof Player player){
                ItemStatManager.onKill(player, event, damagee);
                AbilityManager.onKill(player, event, damagee);
            }
            if(source != null){
                EffectManager.onKill(source, event, damagee);
            }
        }
    }


    //Called by the base shield health class. Maybe refactor to its own event?
    public static void onShieldBlockDamage(Player player, DamageEvent damageEvent){
        AbilityManager.onShieldBlockDamage(player, damageEvent);
    }
}

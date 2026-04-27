package io.github.z.plugin.listeners;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.mobspells.MobSpellManager;
import io.github.z.plugin.utils.ProjectileUtils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void applyEffectEvent(ApplyEffectEvent event){
        Entity source = event.getSourceEntity();
        Entity target = event.getEntity();

        if(source instanceof Player player){
            ItemStatManager.onApplyEffect(player, event);
            AbilityManager.onApplyEffect(player, event);
        } else if(source instanceof LivingEntity le){
            MobSpellManager.onApplyEffect(le, event);
        }
        if(source != null){
            EffectManager.onApplyEffect(source, event);
        }

        if(target instanceof Player player){
            ItemStatManager.onReceiveEffect(player, event);
            AbilityManager.onReceiveEffect(player, event);
        } else if(target instanceof LivingEntity le){
            MobSpellManager.onReceiveEffect(le, event);
        }
        EffectManager.onReceiveEffect(target, event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void entitySpawnEvent(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity le && !(le instanceof Player)) {
            MobSpellManager.getMobSpellManager().grantSpells(le);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void entityDeathEvent(EntityDeathEvent event) {
        MobSpellManager.getMobSpellManager().removeSpells(event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void entityExplodeEvent(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            MobSpellManager.onCreeperExplode(creeper, event);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void entityProjLaunchEvent(ProjectileLaunchEvent event){
        if(event.getEntity().getShooter() instanceof Player player){
            ItemStatManager.onProjectileLaunch(player, event);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void entityShootBowEvent(EntityShootBowEvent event){
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
    public void entityLoadCrossbowEvent(EntityLoadCrossbowEvent event){
        if(event.getEntity() instanceof Player player){
            ItemStatManager.onCrossbowLoad(player, event);
        }
    }

}

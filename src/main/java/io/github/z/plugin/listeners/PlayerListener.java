package io.github.z.plugin.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.utils.DamageUtils;
import io.github.z.plugin.utils.ProjectileUtils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListener implements Listener {

    private static List<Player> playerDropKeyBuffer = new ArrayList<>();

    //WARNING: Asynchronous packet function, do not add API calls.
    public static void registerItemDrop(Player player){
        if(playerDropKeyBuffer.contains(player)){
            return;
        }
        playerDropKeyBuffer.add(player);
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event){
        //Return if player pressed the drop key.
        if(playerDropKeyBuffer.contains(event.getPlayer())){
            playerDropKeyBuffer.remove(event.getPlayer());
            playerPressDropKeyEvent(event.getPlayer());
            return;
        }

        AbilityManager.onClick(event.getPlayer(), event);
    }

    //Note: Not actually an event, called in PlayerInteractEvent
    private void playerPressDropKeyEvent(Player player){
        ItemStatManager.onPlayerDropKey(player);
        AbilityManager.onDropKey(player);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        AbilityManager.playerSwapHandItemsEvent(event);
        event.setCancelled(true);

    }

    @EventHandler
    public void playerFlyEvent(PlayerToggleFlightEvent event){
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
        ItemStatManager.onDoubleJump(event.getPlayer(), event);
    }

    private static final double FALL_DAMAGE_GRACE = 4.5;

    private Map<Player, Boolean> playerGroundedMap = new HashMap<>();

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Boolean wasGrounded = playerGroundedMap.putIfAbsent(player, false);
        Boolean isGrounded = player.isOnGround();
        if(Boolean.FALSE.equals(wasGrounded) && isGrounded){
            playerGroundedMap.put(player, true);
            PlayerLandsOnGroundEvent landingEvent = new PlayerLandsOnGroundEvent(player);
            landingEvent.callEvent();
        }
        else if(Boolean.TRUE.equals(wasGrounded) && !isGrounded){
            playerGroundedMap.put(player, false);
        }
    }

    @EventHandler
    public void playerLandsOnGroundEvent(PlayerLandsOnGroundEvent event){
        Player player = event.getPlayer();

        double damage = player.getFallDistance() - FALL_DAMAGE_GRACE;
        if (damage > 0) {
            if (landedOnDripstone(player)) {
                damage *= 2;
            }
            DamageUtils.nextMetadata = new DamageEvent.Metadata(DamageEvent.DamageType.FALL);
            player.damage(damage);
        }

        ItemStatManager.onPlayerLandsOnGround(player, event);
        AbilityManager.onPlayerLandsOnGround(player, event);
        EffectManager.onPlayerLandsOnGround(player, event);
    }


    //TODO: Make this work (No damage is dealt when landing on dripstone for some reason)
    private boolean landedOnDripstone(Player player) {
        BoundingBox box = player.getBoundingBox();
        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.floor(box.getMaxX());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());
        int y = (int) Math.floor(box.getMinY()) - 1;
        boolean foundDripstone = false;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Material type = player.getWorld().getBlockAt(x, y, z).getType();
                if (type == Material.POINTED_DRIPSTONE) {
                    foundDripstone = true;
                } else if (type.isSolid()) {
                    return false;
                }
            }
        }
        return foundDripstone;
    }

    @EventHandler
    public void playerBreaksItemEvent(PlayerItemBreakEvent event){
        ItemStatManager.onPlayerItemBreak(event.getPlayer(), event);
    }

    @EventHandler
    public void playerDurabilityDamageEvent(PlayerItemDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void entityPotionEffectEvent(EntityPotionEffectEvent event){
        if(event.getEntity() instanceof Player && event.getNewEffect() != null
                && event.getNewEffect().getType().equals(PotionEffectType.WEAKNESS)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerHandItemEvent(PlayerItemHeldEvent event){
        ItemStatManager.updateMainhandStats(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler
    public void playerArmorChangeEvent(PlayerArmorChangeEvent event){
        ItemStatManager.updateArmorStats(event.getPlayer());
    }


}

package io.github.z.plugin.listeners;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.github.z.plugin.itemstats.ItemStatManager;
import io.github.z.plugin.utils.ProjectileUtils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    private Map<Player, Boolean> playerGroundedMap = new HashMap<>();
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event){
        Boolean wasGrounded = playerGroundedMap.putIfAbsent(event.getPlayer(), false);
        Boolean isGrounded = event.getPlayer().isOnGround();
        if(Boolean.FALSE.equals(wasGrounded) && isGrounded){
            playerGroundedMap.put(event.getPlayer(), true);
            PlayerLandsOnGroundEvent landingEvent = new PlayerLandsOnGroundEvent(event.getPlayer());
            landingEvent.callEvent();
        }
        else if(Boolean.TRUE.equals(wasGrounded) && !isGrounded){
            playerGroundedMap.put((event.getPlayer()), false);
        }
    }

    @EventHandler
    public void playerLandsOnGroundEvent(PlayerLandsOnGroundEvent event){
        ItemStatManager.onPlayerLandsOnGround(event.getPlayer(), event);
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
}

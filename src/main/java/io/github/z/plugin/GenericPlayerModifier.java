package io.github.z.plugin;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface GenericPlayerModifier extends GenericEntityModifier{
    //TODO: Migrate to GenericEntityModifier.




    default void onDoubleJump(Player player, PlayerToggleFlightEvent event, double level){

    }

    default void onPlayerLandsOnGround(Player player, PlayerLandsOnGroundEvent event, double level){

    }

    default void onPlayerDropKeyEvent(Player player, double level){

    }
}

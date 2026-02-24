package io.github.z.plugin.itemstats.enchantments;

import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.github.z.plugin.itemstats.Enchantment;
import io.github.z.plugin.utils.DoubleJumpManager;
import io.github.z.plugin.utils.EntityUtils;
import io.github.z.plugin.utils.VectorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Agility implements Enchantment {

    private static Map<Player, Boolean> mCanJump = new HashMap<>();
    private static final double speed = 0.9;
    private static final String doubleJumpToken = "agl";
    @Override
    public String getName() {
        return "agility";
    }

    //TODO: REMOVE DOUBLE JUMP TOKEN WHEN ITEM IS REMOVED

    @Override
    public void onDoubleJump(Player player, PlayerToggleFlightEvent event, double level) {
        if(mCanJump.get(player)){
            mCanJump.put(player, false);
            Vector jumpDirection = player.getLocation().getDirection();
            Vector jumpVelocity = VectorUtils.clampSlope(jumpDirection, 0.3, 3).normalize().multiply(speed);
            EntityUtils.setVelocity(player, jumpVelocity);
            DoubleJumpManager.removeDoubleJumpToken(player, doubleJumpToken);
        }
    }

    @Override
    public void onPlayerLandsOnGround(Player player, PlayerLandsOnGroundEvent event, double level) {
        mCanJump.put(player, true);
        DoubleJumpManager.addDoubleJumpToken(player, doubleJumpToken);
        Bukkit.getLogger().info("Agility wearing player landed on ground!");
    }
}

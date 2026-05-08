package io.github.z.plugin.effects.effects;

import io.github.z.plugin.effects.Effect;
import io.github.z.plugin.effects.PlayerEffect;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import io.github.z.plugin.utils.DoubleJumpManager;
import io.github.z.plugin.utils.EntityUtils;
import io.github.z.plugin.utils.VectorUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class AgilityEffect extends PlayerEffect {

    private static final Map<UUID, Boolean> mCanJump = new HashMap<>();
    private static final double SPEED = 1.0;
    private static final double SPEED_PER_LEVEL = 0.2;
    private static final String DOUBLE_JUMP_TOKEN = "agl_effect";

    public AgilityEffect(int duration, int strength) {
        super(duration, strength);
    }

    @Override
    public String getName() {
        return "Agility";
    }

    @Override
    public void onEffectAdd(Effect oldEffect, Entity entity) {
        if (entity instanceof Player player) {
            DoubleJumpManager.addDoubleJumpToken(player, DOUBLE_JUMP_TOKEN);
            mCanJump.put(entity.getUniqueId(), true);
        }
    }

    @Override
    public void onEffectRemove(Effect newEffect, Entity entity) {
        if (entity instanceof Player player) {
            DoubleJumpManager.removeDoubleJumpToken(player, DOUBLE_JUMP_TOKEN);
            mCanJump.put(player.getUniqueId(), false);
        }
        mCanJump.remove(entity.getUniqueId());
    }

    @Override
    public void onDoubleJump(Player player, PlayerToggleFlightEvent event, double level) {
        Boolean canJump = mCanJump.get(player.getUniqueId());
        if (Boolean.TRUE.equals(canJump)) {
            mCanJump.put(player.getUniqueId(), false);
            Vector jumpVelocity = VectorUtils.clampSlope(player.getLocation().getDirection(), 0.3, 3).normalize().multiply(SPEED + level * SPEED_PER_LEVEL);
            EntityUtils.setVelocity(player, jumpVelocity);
            DoubleJumpManager.removeDoubleJumpToken(player, DOUBLE_JUMP_TOKEN);
        }
    }

    @Override
    public void onPlayerLandsOnGround(Entity entity, PlayerLandsOnGroundEvent event, double level) {
        mCanJump.put(entity.getUniqueId(), true);
        if (entity instanceof Player player) {
            DoubleJumpManager.addDoubleJumpToken(player, DOUBLE_JUMP_TOKEN);
        }
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add(getName() + " " + mStrength + "||" + mDuration);
        return lines;
    }


}

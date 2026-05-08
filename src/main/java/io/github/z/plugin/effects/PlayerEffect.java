package io.github.z.plugin.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.List;

public abstract class PlayerEffect extends Effect {

    public PlayerEffect(int duration, int strength) {
        super(duration, strength);
    }

    public void onDoubleJump(Player player, PlayerToggleFlightEvent event, double level) {}

}

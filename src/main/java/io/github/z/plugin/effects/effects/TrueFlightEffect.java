package io.github.z.plugin.effects.effects;

import io.github.z.plugin.effects.Effect;
import io.github.z.plugin.utils.TrueFlightManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TrueFlightEffect extends Effect {

    private final String TRUE_FLIGHT_TOKEN;

    public TrueFlightEffect(int duration, int strength) {
        super(duration, strength);
        TRUE_FLIGHT_TOKEN = "true_flight_effect";
    }
    public TrueFlightEffect(int duration, int strength, String id){
        super(duration, strength);
        TRUE_FLIGHT_TOKEN = id;
    }

    @Override
    public String getName() {
        return "True Flight";
    }

    @Override
    public void onEffectAdd(Effect oldEffect, Entity entity) {
        if (entity instanceof Player player) {
            TrueFlightManager.addTrueFlightToken(player, TRUE_FLIGHT_TOKEN);
        }
    }

    @Override
    public void onEffectRemove(Effect newEffect, Entity entity) {
        if (entity instanceof Player player) {
            TrueFlightManager.removeTrueFlightToken(player, TRUE_FLIGHT_TOKEN);
        }
    }

    @Override
    public List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add(getName() + " " + mStrength + "||" + mDuration);
        return lines;
    }
}

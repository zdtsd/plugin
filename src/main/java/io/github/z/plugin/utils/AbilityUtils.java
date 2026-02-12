package io.github.z.plugin.utils;

import io.github.z.plugin.abilities.AbilityData;
import org.bukkit.entity.Player;

import java.util.OptionalInt;

public class AbilityUtils {

    public static int getAbilityLevel(Player player, AbilityData<?> data){
        OptionalInt level = ScoreboardUtils.getScore(player, data.getScoreboardID());
        return level.isEmpty() ? 0 : level.getAsInt();
    }

    public static void setAbilityLevel(Player player, AbilityData<?> data, int level){
        ScoreboardUtils.setScore(player, data.getScoreboardID(), level);
    }
}

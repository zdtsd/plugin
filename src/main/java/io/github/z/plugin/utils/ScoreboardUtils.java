package io.github.z.plugin.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.entity.Entity;

import java.util.OptionalInt;

public class ScoreboardUtils {


    public static void setScore(Entity entity, String objectiveName, int value) {
        Bukkit.getLogger().info("ScoreboardUtils setting score to " + value);
        if (entity instanceof Player) {
            setScore(entity.getName(), objectiveName, value);
        } else {
            setScore(entity.getUniqueId().toString(), objectiveName, value);
        }
    }

    public static void setScore(String entry, String objectiveName, int value){

        Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
        if(obj != null){
            Bukkit.getLogger().info("ScoreboardUtils setting " + obj.getName() + " to " + value);
            Score score = obj.getScore(entry);
            score.setScore(value);
        }
    }

    public static OptionalInt getScore(Entity entity, String objectiveName) {
        if (entity instanceof Player) {
            return getScore(entity.getName(), objectiveName);
        } else {
            return getScore(entity.getUniqueId().toString(), objectiveName);
        }
    }

    public static OptionalInt getScore(String entry, String objectiveName){
        Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
        if(obj != null){
            return OptionalInt.of(obj.getScore(entry).getScore());
        }
        return OptionalInt.empty();
    }

    //Gets or creates an objective with the specified name.
    public static Objective createObjective(String name, Component displayName) {
        Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        return obj != null ? obj : Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, Criteria.DUMMY, displayName);
    }


}

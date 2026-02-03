package io.github.z.plugin.utils;

import de.tr7zw.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Projectile;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.OptionalInt;

public class ProjectileUtils {

    public static final String projDamageScoreboardName = "ProjDamageScoreboard";
    public static final String projForceScoreboardName = "ProjForceScoreboard";

    //Default damage for a projectile without modifications.
    private static final int defaultProjDamage = 1;

    //Used to store decimal values in integer scoreboards.
    private static final double damageScalar = 10;
    private static final double forceScalar = 1000;

    public static void setDamage(Projectile projectile, double damage){
        Bukkit.getLogger().info("ProjUtils setting damage to " + (int) (damage * damageScalar));
        ScoreboardUtils.setScore(projectile, projDamageScoreboardName, (int) (damage * damageScalar));
    }

    public static void setForce(Projectile projectile, double force){
        ScoreboardUtils.setScore(projectile, projForceScoreboardName, (int) (force * forceScalar));
    }

    public static double getDamage(Projectile projectile){
        OptionalInt score = ScoreboardUtils.getScore(projectile, projDamageScoreboardName);
        return score.isPresent() && score.getAsInt() > 0 ? ((double) score.getAsInt()) / damageScalar : defaultProjDamage;
    }

    public static double getForce(Projectile projectile){
        OptionalInt score = ScoreboardUtils.getScore(projectile, projForceScoreboardName);
        return score.isPresent() && score.getAsInt() > 0 ? ((double) score.getAsInt()) / forceScalar : 1;
    }

    public ProjectileUtils(){
        Component projDamageDisplayName = Component.text(projDamageScoreboardName);
        ScoreboardUtils.createObjective(projDamageScoreboardName, projDamageDisplayName);

        Component projForceDisplayName = Component.text(projForceScoreboardName);
        ScoreboardUtils.createObjective(projForceScoreboardName, projForceDisplayName);
    }


}

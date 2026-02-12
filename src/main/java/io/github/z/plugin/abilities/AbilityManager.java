package io.github.z.plugin.abilities;

import io.github.z.plugin.abilities.swashbuckler.TestingAbility;
import io.github.z.plugin.utils.AbilityUtils;
import io.github.z.plugin.utils.ScoreboardUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;

import java.util.*;

public class AbilityManager {

    private static AbilityManager abilityManager;
    private final List<AbilityData<?>> mAllAbilities = new ArrayList<>();
    private final Map<Player, AbilitySet> mAbilities = new HashMap<>();

    public AbilityManager(){
        abilityManager = this;

        //Add ALL abilities to mAllAbilities.
        mAllAbilities.add(TestingAbility.DATA);

        //Create a scoreboard for ALL abilities
        for(AbilityData<?> data : mAllAbilities){
            Component abilityDisplayName = Component.text(data.getScoreboardID());
            ScoreboardUtils.createObjective(data.getScoreboardID(), abilityDisplayName);
        }
    }

    public static AbilityManager getAbilityManager(){
        return abilityManager;
    }

    public AbilitySet updateAbilities(Player player){
        AbilitySet returnSet = new AbilitySet();
        if(!player.isOnline()){
            return returnSet;
        }

        for(AbilityData<?> data : mAllAbilities){
            int level = AbilityUtils.getAbilityLevel(player, data);
            if(level != 0){
                Ability ab = data.getNewInstance(player);
                ab.setLevel(level);
                returnSet.addAbility(ab);
            }
        }

        return returnSet;
    }
}

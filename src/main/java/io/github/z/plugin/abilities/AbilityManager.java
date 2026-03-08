package io.github.z.plugin.abilities;

import io.github.z.plugin.abilities.swashbuckler.SwashbuckerShieldBash;
import io.github.z.plugin.abilities.swashbuckler.SwashbucklerReposition;
import io.github.z.plugin.abilities.swashbuckler.SwashbucklerStance;
import io.github.z.plugin.abilities.testing.TestingAbilityOne;
import io.github.z.plugin.abilities.testing.TestingAbilityThree;
import io.github.z.plugin.abilities.testing.TestingAbilityTwo;
import io.github.z.plugin.abilities.testing.TestingAbilityWeakness;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.utils.AbilityUtils;
import io.github.z.plugin.utils.ScoreboardUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.*;

public class AbilityManager {

    private static AbilityManager abilityManager;
    private final List<AbilityData<?>> mAllAbilities = new ArrayList<>();
    private final Map<Player, AbilitySet> mAbilities = new HashMap<>();

    public AbilityManager(){
        abilityManager = this;

        //Add ALL abilities to mAllAbilities.
        mAllAbilities.add(TestingAbilityOne.DATA);
        mAllAbilities.add(TestingAbilityTwo.DATA);
        mAllAbilities.add(TestingAbilityThree.DATA);
        mAllAbilities.add(TestingAbilityWeakness.DATA);
        mAllAbilities.add(SwashbucklerStance.DATA);
        mAllAbilities.add(SwashbucklerReposition.DATA);
        mAllAbilities.add(SwashbuckerShieldBash.DATA);

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

        mAbilities.put(player, returnSet);

        return returnSet;
    }

    public static void tick(Player player, boolean twoHz, boolean oneHz){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.tick(player, twoHz, oneHz);
            if(ability instanceof CooldownAbility cdAbility){
                cdAbility.tickCooldown();
            }
        }
    }

    public static void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event){
        for(Ability ability : abilityManager.mAbilities.get(event.getPlayer()).getAbilities()){
            ability.playerSwapHandItemsEvent(event);
        }
    }

    public static void onDamage(Player player, DamageEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onDamage(event);
        }
    }
    public static void onHurt(Player player, DamageEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onHurt(event);
        }
    }

    public static void onDropKey(Player player){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onDropKey();
        }
    }

    public static void onClick(Player player, PlayerInteractEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onClick(event);
        }
    }

    public static Ability getAbility(Player player, Class<?> abilityClass){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            if(abilityClass.isInstance(ability)){
                return ability;
            }
        }
        return null;
    }

    public List<String> getSidebarLines(Player player){
        List<String> sidebarLines = new ArrayList<>();
        for(Ability ab : mAbilities.get(player).getAbilities()){
            sidebarLines.addAll(ab.getSidebarLines());
        }
        return sidebarLines;
    }
}

package io.github.z.plugin.abilities;

import io.github.z.plugin.abilities.swashbuckler.*;
import io.github.z.plugin.abilities.testing.TestingAbilityOne;
import io.github.z.plugin.abilities.testing.TestingAbilityThree;
import io.github.z.plugin.abilities.testing.TestingAbilityTwo;
import io.github.z.plugin.abilities.testing.TestingAbilityWeakness;
import io.github.z.plugin.events.ApplyEffectEvent;
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
        mAllAbilities.add(SwashbucklerBladeDance.DATA);
        mAllAbilities.add(SwashbucklerSecondWind.DATA);
        mAllAbilities.add(SwashbucklerRiposte.DATA);
        mAllAbilities.add(SwashbucklerAgileDefenses.DATA);

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
    public static void reduceCooldowns(Player player, int ticks){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            if(ability instanceof CooldownAbility cdAbility){
                cdAbility.progressCooldown(ticks);
            }
        }
    }

    public static void reduceCooldownsByPercent(Player player, double percent){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            if(ability instanceof CooldownAbility cdAbility){
                cdAbility.progressCooldown((int)(cdAbility.getData().getCooldown() * percent));
            }
        }
    }

    public static void onApplyEffect(Player player, ApplyEffectEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onApplyEffect(event);
        }
    }

    public static void onReceiveEffect(Player player, ApplyEffectEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onReceiveEffect(event);
        }
    }

    public static void onShieldBlockDamage(Player player, DamageEvent event){
        for(Ability ability : abilityManager.mAbilities.get(player).getAbilities()){
            ability.onShieldBlockDamage(event);
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

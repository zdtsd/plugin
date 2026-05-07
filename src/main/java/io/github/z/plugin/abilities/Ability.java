package io.github.z.plugin.abilities;

import io.github.z.plugin.GenericPlayerModifier;
import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.events.PlayerLandsOnGroundEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Ability {
    private int mLevel = 0;
    protected final Player mPlayer;

    public Ability(Player mPlayer) {
        this.mPlayer = mPlayer;
    }
    public void setLevel(int level){
        mLevel = level;
    }
    public int getLevel(){
        return mLevel;
    }

    public abstract AbilityData<?> getData();

    public void tick(Player player, boolean twoHz, boolean oneHz) {

    }
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {

    }
    public void onDamage(DamageEvent event){

    }

    public void onHurt(DamageEvent event){

    }

    public void onDropKey(){

    }

    public void onClick(PlayerInteractEvent event){

    }

    public void onKill(DamageEvent event, LivingEntity damagee){

    }

    public void onShieldBlockDamage(DamageEvent event){

    }

    public void onApplyEffect(ApplyEffectEvent event){

    }

    public void onReceiveEffect(ApplyEffectEvent event){

    }

    public void onPlayerLandsOnGround(PlayerLandsOnGroundEvent event){

    }

    public List<String> getSidebarLines(){
        return new ArrayList<>();
    }


}

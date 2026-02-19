package io.github.z.plugin.abilities;

import io.github.z.plugin.GenericPlayerModifier;
import io.github.z.plugin.events.DamageEvent;
import org.bukkit.entity.Player;
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

    public List<String> getSidebarLines(){
        return new ArrayList<>();
    }


}

package io.github.z.plugin.abilities;

import io.github.z.plugin.GenericPlayerModifier;
import org.bukkit.entity.Player;

public abstract class Ability {
    private int mLevel = 0;


    public void setLevel(int level){
        mLevel = level;
    }
    public int getLevel(){
        return mLevel;
    }

    public abstract AbilityData<?> getData();

    public void tick(Player player, boolean twoHz, boolean oneHz) {

    }
}

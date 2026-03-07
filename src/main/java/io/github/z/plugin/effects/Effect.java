package io.github.z.plugin.effects;

import io.github.z.plugin.GenericEntityModifier;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//TODO: Refactor to not use GenericEntityModifier
public abstract class Effect implements GenericEntityModifier, Comparable<Effect> {
    protected int mDuration = 0;
    protected int mStrength = 1;

    public Effect(int duration, int strength){
        mDuration = duration;
        mStrength = strength;
    }

    public void reduceDuration(int ticks){
        mDuration -= ticks;
    }

    public int getDuration(){
        return mDuration;
    }

    public int getStrength(){
        return mStrength;
    }

    @Override
    public int compareTo(@NotNull Effect o) {
        if(o.getStrength() > getStrength()){
            return 1;
        }
        else if(getStrength() > o.getStrength()){
            return -1;
        }
        else if(o.getDuration() > getDuration()){
            return 1;
        }
        else if(getDuration() > o.getDuration()){
            return -1;
        }
        else{
            return 0;
        }
    }

    List<String> getSidebarLines(){return new ArrayList<>();}


    public void onEffectRemove(Effect newEffect, Entity entity){

    }
    public void onEffectAdd(Effect oldEffect, Entity entity){

    }
}

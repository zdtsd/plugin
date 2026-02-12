package io.github.z.plugin.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Function;


//Each Ability class should have a static AbilityData field which records information about the class.

public class AbilityData<T extends Ability> {
    private final Class<T> mAbility;
    private final Function<Player, T> mConstructor;
    private final String mName;
    private String mScoreboardID = null;
    private Material mDisplayItem = null;
    private double mPriorityAmount = 1000;


    public AbilityData(Class<T> abilityClass, String name, Function<Player, T> constructor){
        mAbility = abilityClass;
        mName = name;
        mConstructor = constructor;
    }

    //Builder functions
    public AbilityData<T> scoreboardID(String id){
        mScoreboardID = id;
        return this;
    }

    public AbilityData<T> displayMaterial(Material material){
        mDisplayItem = material;
        return this;
    }

    public AbilityData<T> priority(double prio){
        mPriorityAmount = prio;
        return this;
    }


    //Getters
    public String getScoreboardID(){
        return mScoreboardID;
    }

    public Ability getNewInstance(Player player){
        return mConstructor.apply(player);
    }


}

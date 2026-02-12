package io.github.z.plugin.abilities;

import io.github.z.plugin.GenericPlayerModifier;

public abstract class Ability {
    private int mLevel = 0;


    public void setLevel(int level){
        mLevel = level;
    }
    public int getLevel(){
        return mLevel;
    }
}

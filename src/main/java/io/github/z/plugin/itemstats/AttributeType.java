package io.github.z.plugin.itemstats;

import io.github.z.plugin.itemstats.attributes.*;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;

import java.util.Collections;
import java.util.Set;

public enum AttributeType {
    //Custom Attributes
    CUSTOM_DEFENSE(new Defense(), "Defense"),


    //Vanilla Attributes
    ARMOR(Attribute.GENERIC_ARMOR, "Armor", false),
    ARMOR_PERCENT(Attribute.GENERIC_ARMOR, "% Armor", true),
    MAX_HEALTH(Attribute.GENERIC_MAX_HEALTH, "Max Health", false),
    MAX_HEALTH_PERCENT(Attribute.GENERIC_MAX_HEALTH, "% Max Health", true),
    MOVEMENT_SPEED(Attribute.GENERIC_MOVEMENT_SPEED, "Speed", false),
    MOVEMENT_SPEED_PERCENT(Attribute.GENERIC_MOVEMENT_SPEED, "% Speed", true),
    ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, "Attack Speed", false),
    ATTACK_SPEED_PERCENT(Attribute.GENERIC_ATTACK_SPEED, "% Attack Speed", true),

    //Hidden attributes (Represented by enchantments)
    DEPTH_STRIDER(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY, "", false);

    public static final String PERCENTAGE_STRING = "_percent";
    private final ItemStat mStat;
    private final Attribute mVanillaStat;
    private final String mName;
    private final String mDisplayName;
    private final boolean mIsVanillaPercentage;

    AttributeType(ItemStat stat, String displayName) {
        mStat = stat;
        mVanillaStat = null;
        mName = stat.getName();
        mIsVanillaPercentage = false;
        mDisplayName = displayName;
    }
    AttributeType(Attribute stat, String displayName, boolean isPercentage) {
        mStat = null;
        mVanillaStat = stat;
        mName = isPercentage ? stat.getKey().getKey() + PERCENTAGE_STRING : stat.getKey().getKey();
        mIsVanillaPercentage = isPercentage;
        mDisplayName = displayName;
    }

    public String getName(){
        return mName;
    }

    public String getDisplayName(){
        return mDisplayName;
    };

    public ItemStat getStat(){
        return mStat;
    }
    public Attribute getVanillaStat(){
        return mVanillaStat;
    }

    public boolean isVanilla(){ return mVanillaStat != null;}
    private static final Set<AttributeType> VanillaAttributes = Collections.emptySet();

    public boolean isVanillaPercentage(){
        return mIsVanillaPercentage;
    }
}

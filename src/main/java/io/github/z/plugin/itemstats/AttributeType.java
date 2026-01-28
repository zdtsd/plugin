package io.github.z.plugin.itemstats;

import io.github.z.plugin.itemstats.attributes.*;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;

import java.util.Collections;
import java.util.Set;

public enum AttributeType {
    //Custom Attributes
    CUSTOM_DEFENSE(new Defense(), "Defense"),
    PROJ_DAMAGE_PERCENT(new PercentProjDamage(), "% Projectile Damage"),
    ATTACK_DAMAGE_BASE(new BaseAttackDamage(), "Attack Damage", true),
    ATTACK_SPEED_BASE(new BaseAttackSpeed(), "Attack Speed", true),
    MELEE_DAMAGE(new MeleeDamageAdd(), "Melee Damage"),
    MELEE_DAMAGE_PERCENT(new MeleeDamagePercent(), "% Melee Damage"),


    //Vanilla Attributes
    ARMOR(Attribute.GENERIC_ARMOR, "Armor", false),
    ARMOR_PERCENT(Attribute.GENERIC_ARMOR, "% Armor", true),
    MAX_HEALTH(Attribute.GENERIC_MAX_HEALTH, "Max Health", false),
    MAX_HEALTH_PERCENT(Attribute.GENERIC_MAX_HEALTH, "% Max Health", true),
    MOVEMENT_SPEED(Attribute.GENERIC_MOVEMENT_SPEED, "Speed", false),
    MOVEMENT_SPEED_PERCENT(Attribute.GENERIC_MOVEMENT_SPEED, "% Speed", true),
    ATTACK_SPEED_PERCENT(Attribute.GENERIC_ATTACK_SPEED, "% Attack Speed", true),

    //Hidden attributes (Represented by enchantments)

    ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, "REAL ATTACK SPEED", false),
    DEPTH_STRIDER(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY, "", false);

    public static final String PERCENTAGE_STRING = "_percent";
    private final ItemStat mStat;
    private final Attribute mVanillaStat;
    private final String mName;
    private final String mDisplayName;
    private final boolean mIsVanillaPercentage;
    private final boolean mIsBaseStat;

    AttributeType(ItemStat stat, String displayName) {
        this(stat, displayName, false);
    }
    AttributeType(ItemStat stat, String displayName, boolean isBaseStat) {
        mStat = stat;
        mVanillaStat = null;
        mName = stat.getName();
        mIsVanillaPercentage = false;
        mDisplayName = displayName;
        mIsBaseStat = isBaseStat;
    }
    AttributeType(Attribute stat, String displayName, boolean isPercentage) {
        mStat = null;
        mVanillaStat = stat;
        mName = isPercentage ? stat.getKey().getKey() + PERCENTAGE_STRING : stat.getKey().getKey();
        mIsVanillaPercentage = isPercentage;
        mDisplayName = displayName;
        mIsBaseStat = false;
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
    public boolean isVanillaPercentage(){
        return mIsVanillaPercentage;
    }

    public boolean isBaseStat(){
        return mIsBaseStat;
    }
}

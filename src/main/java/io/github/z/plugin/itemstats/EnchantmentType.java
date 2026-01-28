package io.github.z.plugin.itemstats;

import io.github.z.plugin.itemstats.enchantments.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;

import java.util.MissingFormatArgumentException;
import java.util.Objects;

public enum EnchantmentType {
    //Custom enchantments
    //TESTING
    TESTING1(new TestingOne(), "Testing 1", true, false),
    TESTING4(new TestingFour(), "Testing 4", true, false),
    LOG_DAMAGE(new LogDamage(), "Log Damage", false, false),

    //REAL
    DEPTH_STRIDER(new DepthStrider(), "Depth Strider", true, false),
    POINT_BLANK(new PointBlank(), "Point Blank", true, false),

    //Vanilla Enchantments
    CURSE_OF_VANISHING(Enchantment.VANISHING_CURSE, "Curse of Vanishing", false, true);

    private final io.github.z.plugin.itemstats.Enchantment mStat;
    private final Enchantment mVanillaStat;
    private final String mName;
    private final String mDisplayName;
    private final boolean mHasLevels;
    private final boolean mIsCurse;

    EnchantmentType(io.github.z.plugin.itemstats.Enchantment stat, String displayName, boolean hasLevels, boolean isCurse) {
        mStat = stat;
        mVanillaStat = null;
        mName = stat.getName();
        mDisplayName = displayName;
        mHasLevels = hasLevels;
        mIsCurse = isCurse;
       // mLockedSlot = null;
    }
    EnchantmentType(Enchantment stat, String displayName, boolean hasLevels, boolean isCurse) {
        mStat = null;
        mVanillaStat = stat;
        mName = stat.getName();
        mDisplayName = displayName;
        mHasLevels = hasLevels;
        mIsCurse = isCurse;
        //mLockedSlot = null;
    }

    public io.github.z.plugin.itemstats.Enchantment getItemStat(){return mStat;}
    public Enchantment getVanillaStat(){return mVanillaStat;}
    public String getName(){
        return mName;
    }
    public String getDisplayName(){return mDisplayName;};
    public boolean getIsCurse(){return mIsCurse;}
    public boolean getHasLevels(){return mHasLevels;}
    public boolean isVanilla(){return mVanillaStat != null;}

    public static EnchantmentType getTypeFromName(String name){
        //TODO: Use a reverse map instead of a string lookup.
        for(EnchantmentType type : EnchantmentType.values()){
            if(Objects.equals(type.getName(), name)){
                return type;
            }
        }
        return null;
    }
}

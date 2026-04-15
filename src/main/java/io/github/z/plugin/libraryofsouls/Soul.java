package io.github.z.plugin.libraryofsouls;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Soul {
    public static final double DEFAULT_MAX_HEALTH = 20.0;
    public static final double DEFAULT_ATTACK_DAMAGE = 4.0;
    public static final double DEFAULT_MOVEMENT_SPEED = 0.15;

    private String mId;
    private EntityType mType;
    private double mMaxHealth;
    private double mAttackDamage;
    private double mMovementSpeed;
    private Map<String, Integer> mMobSpells;

    public Soul(String id, EntityType type) {
        mId = id;
        mType = type;
        mMaxHealth = DEFAULT_MAX_HEALTH;
        mAttackDamage = DEFAULT_ATTACK_DAMAGE;
        mMovementSpeed = DEFAULT_MOVEMENT_SPEED;
        mMobSpells = new HashMap<>();
    }

    public Soul(String id, EntityType type, double maxHealth, double attackDamage, double movementSpeed, Map<String, Integer> mobSpells) {
        mId = id;
        mType = type;
        mMaxHealth = maxHealth;
        mAttackDamage = attackDamage;
        mMovementSpeed = movementSpeed;
        mMobSpells = new HashMap<>(mobSpells);
    }

    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public EntityType getType() { return mType; }
    public void setType(EntityType type) { mType = type; }

    public double getMaxHealth() { return mMaxHealth; }
    public void setMaxHealth(double maxHealth) { mMaxHealth = maxHealth; }

    public double getAttackDamage() { return mAttackDamage; }
    public void setAttackDamage(double attackDamage) { mAttackDamage = attackDamage; }

    public double getMovementSpeed() { return mMovementSpeed; }
    public void setMovementSpeed(double movementSpeed) { mMovementSpeed = movementSpeed; }

    public Map<String, Integer> getMobSpells() { return mMobSpells; }
    public void setMobSpells(Map<String, Integer> mobSpells) { mMobSpells = new HashMap<>(mobSpells); }

    public void setMobSpell(String spellName, int level) {
        if (level == 0) {
            mMobSpells.remove(spellName);
        } else {
            mMobSpells.put(spellName, level);
        }
    }
}

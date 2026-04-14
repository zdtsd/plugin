package io.github.z.plugin.mobspells;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.mobspells.testing.TestingLogOnHurt;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.*;

public class MobSpellManager {

    private static MobSpellManager mobSpellManager;
    private final List<MobSpellData<?>> mAllSpells = new ArrayList<>();
    private final Map<UUID, MobSpellSet> mSpells = new HashMap<>();

    private static final String SPELLS_NBT_TAG = "mobSpells";
    private static final String LEVEL_NBT_TAG = "level";

    public MobSpellManager() {
        mobSpellManager = this;

        // Register all mob spells here
        mAllSpells.add(TestingLogOnHurt.DATA);
    }

    public static MobSpellManager getMobSpellManager() {
        return mobSpellManager;
    }

    /**
     * Reads the entity's NBT and grants any registered mob spells found there.
     *
     * <p>Expected NBT structure on the entity:
     * <pre>
     * mobSpells (compound)
     * └── [SpellName] (compound)   -- must match MobSpellData.getName() exactly
     *     └── level (int)          -- spell level; 0 is ignored
     * </pre>
     *
     * <p>Example summon command:
     * <pre>
     * /summon zombie ~ ~ ~ {mobSpells:{MySpellName:{level:1}}}
     * </pre>
     */
    public void grantSpells(LivingEntity entity) {
        MobSpellSet spellSet = new MobSpellSet();

        //TODO: Make spell tag get found somehow
        NBT.get(entity, nbt -> {
            for(String s : nbt.getKeys()){
                Bukkit.getLogger().info(s + " tag found");
            }
            ReadableNBT spellsNBT = nbt.getCompound(SPELLS_NBT_TAG);
            if (spellsNBT == null) {
                Bukkit.getLogger().info("Spell Tag NOT Found!");
                return;
            }
            Bukkit.getLogger().info("Spell Tag Found!");

            for (MobSpellData<?> data : mAllSpells) {
                ReadableNBT spellNBT = spellsNBT.getCompound(data.getName());
                if (spellNBT != null) {
                    Bukkit.getLogger().info("Spell Compound Found!");
                    int level = spellNBT.getInteger(LEVEL_NBT_TAG);
                    if (level != 0) {
                        MobSpell spell = data.getNewInstance(entity);
                        spell.setLevel(level);
                        spellSet.addSpell(spell);
                        Bukkit.getLogger().info("Spell added!");
                    }
                }
            }
        });

        if (!spellSet.getSpells().isEmpty()) {
            mSpells.put(entity.getUniqueId(), spellSet);
        }
    }

    public void removeSpells(LivingEntity entity) {
        mSpells.remove(entity.getUniqueId());
    }

    public static boolean hasSpells(LivingEntity entity) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        return set != null && !set.getSpells().isEmpty();
    }

    // 4Hz
    public static void tick(boolean twoHz, boolean oneHz) {
        List<UUID> toRemove = new ArrayList<>();

        for (Map.Entry<UUID, MobSpellSet> entry : mobSpellManager.mSpells.entrySet()) {
            List<MobSpell> spells = entry.getValue().getSpells();
            if (spells.isEmpty()) {
                continue;
            }
            LivingEntity entity = spells.get(0).getEntity();
            if (!entity.isValid() || entity.isDead()) {
                toRemove.add(entry.getKey());
                continue;
            }
            for (MobSpell spell : spells) {
                spell.tick(entity, twoHz, oneHz);
            }
        }

        for (UUID uuid : toRemove) {
            mobSpellManager.mSpells.remove(uuid);
        }
    }

    public static void onDamage(LivingEntity entity, DamageEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) {
            return;
        }
        for (MobSpell spell : set.getSpells()) {
            spell.onDamage(event);
        }
    }

    public static void onHurt(LivingEntity entity, DamageEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) {
            return;
        }
        for (MobSpell spell : set.getSpells()) {
            spell.onHurt(event);
        }
    }

    public static void onApplyEffect(LivingEntity entity, ApplyEffectEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) {
            return;
        }
        for (MobSpell spell : set.getSpells()) {
            spell.onApplyEffect(event);
        }
    }

    public static void onReceiveEffect(LivingEntity entity, ApplyEffectEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) {
            return;
        }
        for (MobSpell spell : set.getSpells()) {
            spell.onReceiveEffect(event);
        }
    }

    public static MobSpell getSpell(LivingEntity entity, Class<?> spellClass) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) {
            return null;
        }
        for (MobSpell spell : set.getSpells()) {
            if (spellClass.isInstance(spell)) {
                return spell;
            }
        }
        return null;
    }
}

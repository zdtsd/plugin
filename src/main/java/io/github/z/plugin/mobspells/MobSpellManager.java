package io.github.z.plugin.mobspells;

import io.github.z.plugin.Plugin;
import io.github.z.plugin.events.ApplyEffectEvent;
import io.github.z.plugin.events.DamageEvent;
import io.github.z.plugin.mobspells.testing.TestingLogOnHurt;
import io.github.z.plugin.mobspells.passives.Gravedigger;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MobSpellManager {

    private static MobSpellManager mobSpellManager;
    private final List<MobSpellData<?>> mAllSpells = new ArrayList<>();
    private final Map<UUID, MobSpellSet> mSpells = new HashMap<>();

    private static final NamespacedKey SPELLS_KEY = new NamespacedKey(Plugin.getPlugin(), "mob_spells");

    public MobSpellManager() {
        mobSpellManager = this;

        // Register all mob spells here
        mAllSpells.add(TestingLogOnHurt.DATA);
        mAllSpells.add(Gravedigger.DATA);
    }

    public static MobSpellManager getMobSpellManager() {
        return mobSpellManager;
    }

    public List<MobSpellData<?>> getAllSpells() {
        return Collections.unmodifiableList(mAllSpells);
    }

    /**
     * Reads spell data from the entity's Persistent Data Container and grants
     * any registered mob spells found there.
     *
     * PDC structure on the entity:
     *   mob_spells (TAG_CONTAINER)
     *   └── [SpellName] (INTEGER) — spell level; 0 is ignored
     *
     * Set spells on an entity programmatically via applySpells().
     */
    public void grantSpells(LivingEntity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (!pdc.has(SPELLS_KEY, PersistentDataType.TAG_CONTAINER)) {
            return;
        }

        PersistentDataContainer spellsContainer = pdc.get(SPELLS_KEY, PersistentDataType.TAG_CONTAINER);
        MobSpellSet spellSet = new MobSpellSet();

        for (MobSpellData<?> data : mAllSpells) {
            NamespacedKey spellKey = new NamespacedKey(Plugin.getPlugin(), data.getName().toLowerCase());
            if (spellsContainer.has(spellKey, PersistentDataType.INTEGER)) {
                int level = spellsContainer.get(spellKey, PersistentDataType.INTEGER);
                if (level != 0) {
                    MobSpell spell = data.getNewInstance(entity);
                    spell.setLevel(level);
                    spellSet.addSpell(spell);
                }
            }
        }

        if (!spellSet.getSpells().isEmpty()) {
            mSpells.put(entity.getUniqueId(), spellSet);
        }
    }

    /**
     * Writes spell data onto the entity's Persistent Data Container and grants
     * the spells immediately. Replaces any existing spells on the entity.
     *
     * @param spells map of spell name (must match a registered MobSpellData name) to level
     */
    public static void applySpells(LivingEntity entity, Map<String, Integer> spells) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        PersistentDataContainer spellsContainer = pdc.getAdapterContext().newPersistentDataContainer();

        for (Map.Entry<String, Integer> entry : spells.entrySet()) {
            NamespacedKey spellKey = new NamespacedKey(Plugin.getPlugin(), entry.getKey().toLowerCase());
            spellsContainer.set(spellKey, PersistentDataType.INTEGER, entry.getValue());
        }

        pdc.set(SPELLS_KEY, PersistentDataType.TAG_CONTAINER, spellsContainer);
        mobSpellManager.removeSpells(entity);
        mobSpellManager.grantSpells(entity);
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
        if (set == null) return;
        for (MobSpell spell : set.getSpells()) {
            spell.onDamage(event);
        }
    }

    public static void onHurt(LivingEntity entity, DamageEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) return;
        for (MobSpell spell : set.getSpells()) {
            spell.onHurt(event);
        }
    }

    public static void onApplyEffect(LivingEntity entity, ApplyEffectEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) return;
        for (MobSpell spell : set.getSpells()) {
            spell.onApplyEffect(event);
        }
    }

    public static void onReceiveEffect(LivingEntity entity, ApplyEffectEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) return;
        for (MobSpell spell : set.getSpells()) {
            spell.onReceiveEffect(event);
        }
    }

    public static void onCreeperExplode(LivingEntity entity, EntityExplodeEvent event) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) return;
        for (MobSpell spell : set.getSpells()) {
            spell.onCreeperExplode(event);
        }
    }

    public static MobSpell getSpell(LivingEntity entity, Class<?> spellClass) {
        MobSpellSet set = mobSpellManager.mSpells.get(entity.getUniqueId());
        if (set == null) return null;
        for (MobSpell spell : set.getSpells()) {
            if (spellClass.isInstance(spell)) {
                return spell;
            }
        }
        return null;
    }
}

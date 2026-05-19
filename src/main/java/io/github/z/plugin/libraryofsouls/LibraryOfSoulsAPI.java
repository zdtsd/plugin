package io.github.z.plugin.libraryofsouls;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.z.plugin.mobspells.MobSpellManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class LibraryOfSoulsAPI {

    private static LibraryOfSoulsAPI instance;

    private final SoulDatabase mDatabase;

    public LibraryOfSoulsAPI(SoulDatabase database) {
        mDatabase = database;
        instance = this;
    }

    public static LibraryOfSoulsAPI getInstance() {
        return instance;
    }

    public SoulDatabase getDatabase() {
        return mDatabase;
    }

    public static StringArgument soulIdArgument(String nodeName) {
        return (StringArgument) new StringArgument(nodeName)
                .replaceSuggestions(ArgumentSuggestions.stringsAsync(info ->
                        CompletableFuture.supplyAsync(() ->
                                getInstance().getDatabase().getAllSoulIds().toArray(new String[0])
                        )
                ));
    }

    public void createSoul(Player player, String id) {
        if (mDatabase.idExists(id)) {
            player.sendMessage(Component.text("A Soul with ID '" + id + "' already exists.", NamedTextColor.RED));
            return;
        }
        Soul soul = new Soul(id, EntityType.ZOMBIE);
        mDatabase.insertSoul(soul);
        player.getInventory().addItem(BookOfSoulsUtils.createBookOfSouls(id));
    }

    public void giveBookOfSouls(Player player, String id) {
        if (!mDatabase.idExists(id)) {
            player.sendMessage(Component.text("No Soul with ID '" + id + "' exists.", NamedTextColor.RED));
            return;
        }
        player.getInventory().addItem(BookOfSoulsUtils.createBookOfSouls(id));
    }

    public void deleteSoul(String id) {
        mDatabase.deleteSoul(id);
    }

    public void openLibraryMenu(Player player) {
        new io.github.z.plugin.gui.GUIs.LibraryOfSoulsGUI(player, 0);
    }

    public void setNBTTag(String id, String tag, double value) {
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        switch (tag) {
            case "max_health"      -> soul.setMaxHealth(value);
            case "attack_damage"   -> soul.setAttackDamage(value);
            case "movement_speed"  -> soul.setMovementSpeed(value);
        }
        mDatabase.updateSoul(soul);
    }

    public void setID(Player player, String id, String newId) {
        if (mDatabase.idExists(newId)) {
            player.sendMessage(Component.text("A Soul with ID '" + newId + "' already exists.", NamedTextColor.RED));
            return;
        }
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        mDatabase.deleteSoul(id);
        soul.setId(newId);
        mDatabase.insertSoul(soul);
    }

    public void setType(String id, EntityType type) {
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        soul.setType(type);
        mDatabase.updateSoul(soul);
    }

    public void setAbility(String id, String spellName, int level) {
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        soul.setMobSpell(spellName, level);
        mDatabase.updateSoul(soul);
    }

    public void setEntityData(String id, String key, String value) {
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        soul.setEntityDataValue(key, value);
        mDatabase.updateSoul(soul);
    }

    public void openSoulGUI(Player player, String id) {
        Soul soul = mDatabase.getSoul(id);
        if (soul == null) return;
        new io.github.z.plugin.gui.GUIs.SoulGUI(player, soul);
    }

    public LivingEntity summon(Location location, Soul soul) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, soul.getType());

        setAttribute(entity, Attribute.GENERIC_MAX_HEALTH, soul.getMaxHealth());
        entity.setHealth(soul.getMaxHealth());
        setAttribute(entity, Attribute.GENERIC_ATTACK_DAMAGE, soul.getAttackDamage());
        setAttribute(entity, Attribute.GENERIC_MOVEMENT_SPEED, soul.getMovementSpeed());

        for (java.util.Map.Entry<String, String> entry : soul.getEntityData().entrySet()) {
            EntityDataKey key = EntityDataKey.fromKey(entry.getKey());
            if (key != null) key.apply(entity, entry.getValue());
        }

        if (!soul.getMobSpells().isEmpty()) {
            MobSpellManager.applySpells(entity, soul.getMobSpells());
        }

        return entity;
    }

    private void setAttribute(LivingEntity entity, Attribute attribute, double value) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }
}

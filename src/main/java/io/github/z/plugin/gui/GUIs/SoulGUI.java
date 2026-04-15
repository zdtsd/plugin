package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import io.github.z.plugin.libraryofsouls.Soul;
import io.github.z.plugin.protocollib.ProtocolLibUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class SoulGUI extends GUI {

    public static final int GUI_SIZE = 27;

    private final Soul mSoul;

    public SoulGUI(Player player, Soul soul) {
        super(player, GUI_SIZE, Component.text(soul.getId()), Material.BLACK_STAINED_GLASS_PANE, false);
        mSoul = soul;
        setupGUI();
        buildMenu();
        open();
    }

    @Override
    protected void setupGUI() {
        String id = mSoul.getId();

        // Slot 0 — ID
        new GUIButton()
                .setMaterial(Material.NAME_TAG)
                .setName(Component.text("ID"))
                .addLore(Component.text(id))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos id " + id + " ");
                    }
                })
                .addToGUI(this, 0);

        // Slot 1 — Entity Type
        new GUIButton()
                .setMaterial(Material.SPAWNER)
                .setName(Component.text("Entity Type"))
                .addLore(Component.text(mSoul.getType().name()))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos type " + id + " ");
                    }
                })
                .addToGUI(this, 1);

        // Slot 2 — max_health
        new GUIButton()
                .setMaterial(Material.RED_GLAZED_TERRACOTTA)
                .setName(Component.text("Max Health"))
                .addLore(Component.text(String.valueOf(mSoul.getMaxHealth())))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos nbt " + "max_health " + id + " ");
                    }
                })
                .addToGUI(this, 2);

        // Slot 3 — attack_damage
        new GUIButton()
                .setMaterial(Material.IRON_SWORD)
                .setName(Component.text("Attack Damage"))
                .addLore(Component.text(String.valueOf(mSoul.getAttackDamage())))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos nbt " + "attack_damage " + id + " ");
                    }
                })
                .addToGUI(this, 3);

        // Slot 4 — movement_speed
        new GUIButton()
                .setMaterial(Material.FEATHER)
                .setName(Component.text("Movement Speed"))
                .addLore(Component.text(String.valueOf(mSoul.getMovementSpeed())))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos nbt " + "movement_speed " + id + " ");
                    }
                })
                .addToGUI(this, 4);

        // Slot 5 — MobSpells
        GUIButton spellsButton = new GUIButton()
                .setMaterial(Material.BOOK)
                .setName(Component.text("Mob Spells"))
                .addHandler(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                        ProtocolLibUtils.autofillChat(player, "/bos ability " + id + " ");
                    }
                });
        Map<String, Integer> spells = mSoul.getMobSpells();
        if (spells.isEmpty()) {
            spellsButton.addLore(Component.text("None"));
        } else {
            for (Map.Entry<String, Integer> entry : spells.entrySet()) {
                spellsButton.addLore(Component.text(entry.getKey() + ": " + entry.getValue()));
            }
        }
        spellsButton.addToGUI(this, 5);
    }
}

package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.abilities.Ability;
import io.github.z.plugin.abilities.AbilityData;
import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.testing.TestingAbilityOne;
import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import io.github.z.plugin.utils.AbilityUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class AbilitySelectGUI extends GUI {

    public AbilitySelectGUI(Player player, int size) {
        super(player, size);
    }

    public AbilitySelectGUI(Player player, int size, Component title) {
        super(player, size, title);
    }

    public AbilitySelectGUI(Player player, int size, Component title, Material placeholder) {
        super(player, size, title, placeholder);
    }

    protected void createClassAbilityButton(AbilityData<?> ability, int slot){
        int level = AbilityUtils.getAbilityLevel(getPlayer(), ability);

        new GUIButton()
                .setMaterial(level == 0 ? Material.BARRIER : ability.getDisplayItem())
                .setName(Component.text(ability.getScoreboardID()))
                .addHandler((clickEvent) -> {
                    int setTo = level == 0 ? 1 : 0;
                    AbilityUtils.setAbilityLevel(getPlayer(),  ability, setTo);
                    AbilityManager.getAbilityManager().updateAbilities(getPlayer());
                    fullRebuild();
                })
                .addToGUI(this, slot);
    }
}

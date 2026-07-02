package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.abilities.AbilityManager;
import io.github.z.plugin.abilities.testing.TestingAbilityArcParticle;
import io.github.z.plugin.abilities.testing.TestingAbilityOne;
import io.github.z.plugin.abilities.testing.TestingAbilityThree;
import io.github.z.plugin.abilities.testing.TestingAbilityTwo;
import io.github.z.plugin.abilities.testing.TestingAbilityWeakness;
import io.github.z.plugin.abilities.testing.TestingCutscene;
import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import io.github.z.plugin.utils.AbilityUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestingClassGUI extends AbilitySelectGUI {
    public static final int GUI_SIZE = 6 * 9;

    public TestingClassGUI(Player player, int size) {
        super(player, size);
    }

    public TestingClassGUI(Player player, int size, Component title) {
        super(player, size, title);
    }

    public TestingClassGUI(Player player, int size, Component title, Material placeholder) {
        super(player, size, title, placeholder);
    }

    @Override
    protected void setupGUI(){
        createClassAbilityButton(TestingAbilityOne.DATA, 13);
        createClassAbilityButton(TestingAbilityTwo.DATA, 31);
        createClassAbilityButton(TestingAbilityThree.DATA, 49);
        createClassAbilityButton(TestingAbilityWeakness.DATA, 4);
        createClassAbilityButton(TestingCutscene.DATA, 22);
        createClassAbilityButton(TestingAbilityArcParticle.DATA, 40);
    }


}

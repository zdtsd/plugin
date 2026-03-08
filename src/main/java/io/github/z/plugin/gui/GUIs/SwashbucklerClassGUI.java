package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.abilities.swashbuckler.SwashbuckerShieldBash;
import io.github.z.plugin.abilities.swashbuckler.SwashbucklerReposition;
import io.github.z.plugin.abilities.swashbuckler.SwashbucklerStance;
import io.github.z.plugin.abilities.testing.TestingAbilityOne;
import io.github.z.plugin.abilities.testing.TestingAbilityThree;
import io.github.z.plugin.abilities.testing.TestingAbilityTwo;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SwashbucklerClassGUI extends AbilitySelectGUI {
    public static final int GUI_SIZE = 6 * 9;

    public SwashbucklerClassGUI(Player player, int size) {
        super(player, size);
    }

    public SwashbucklerClassGUI(Player player, int size, Component title) {
        super(player, size, title);
    }

    public SwashbucklerClassGUI(Player player, int size, Component title, Material placeholder) {
        super(player, size, title, placeholder);
    }

    @Override
    protected void setupGUI(){
        createClassAbilityButton(SwashbucklerStance.DATA, 4);
        createClassAbilityButton(SwashbucklerReposition.DATA, 13);
        createClassAbilityButton(SwashbuckerShieldBash.DATA, 22);
    }


}

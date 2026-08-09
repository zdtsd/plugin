package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.abilities.stormcaller.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StormcallerClassGUI extends AbilitySelectGUI {
    public static final int GUI_SIZE = 6 * 9;

    public StormcallerClassGUI(Player player, int size) {
        super(player, size);
    }

    public StormcallerClassGUI(Player player, int size, Component title) {
        super(player, size, title);
    }

    public StormcallerClassGUI(Player player, int size, Component title, Material placeholder) {
        super(player, size, title, placeholder);
    }

    @Override
    protected void setupGUI() {
        createClassAbilityButton(StormcallerStaticCharge.DATA, 4);
        createClassAbilityButton(StormcallerZap.DATA, 13);
        createClassAbilityButton(StormcallerBoltwave.DATA, 22);
        createClassAbilityButton(StormcallerStrikeTwice.DATA, 31);
        createClassAbilityButton(StormcallerVoltstep.DATA, 40);
    }
}

package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ClassGUI extends GUI {
    public static final int GUI_SIZE = 1 * 9;

    public ClassGUI(Player player, int size) {
        super(player, size);
    }

    public ClassGUI(Player player, int size, Component title) {
        super(player, size, title);
    }

    public ClassGUI(Player player, int size, Component title, Material placeholder) {
        super(player, size, title, placeholder);
    }

    @Override
    protected void setupGUI(){
        new GUIButton()
                .setMaterial(Material.BARRIER)
                .setName(Component.text("Testing"))
                .addHandler((clickEvent) -> {
                    if(clickEvent.getWhoClicked() instanceof Player player){
                        new TestingClassGUI(player, TestingClassGUI.GUI_SIZE);
                    }
                })
                .addToGUI(this, 0);
        new GUIButton()
                .setMaterial(Material.SHIELD)
                .setName(Component.text("Swashbuckler"))
                .addHandler((clickEvent) -> {
                    if(clickEvent.getWhoClicked() instanceof Player player){
                        new SwashbucklerClassGUI(player, SwashbucklerClassGUI.GUI_SIZE);
                    }
                })
                .addToGUI(this, 2);
        new GUIButton()
                .setMaterial(Material.CROSSBOW)
                .setName(Component.text("Artificer"))
                .addHandler((clickEvent) -> {
                    Bukkit.getLogger().info("Artificer selected!");
                })
                .addToGUI(this, 4);
        new GUIButton()
                .setMaterial(Material.TRIDENT)
                .setName(Component.text("Stormcaller"))
                .addHandler((clickEvent) -> {
                    if(clickEvent.getWhoClicked() instanceof Player player){
                        new StormcallerClassGUI(player, StormcallerClassGUI.GUI_SIZE);
                    }
                })
                .addToGUI(this, 6);
    }

}

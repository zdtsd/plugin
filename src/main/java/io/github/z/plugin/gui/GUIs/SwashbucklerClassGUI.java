package io.github.z.plugin.gui.GUIs;

import io.github.z.plugin.gui.GUI;
import io.github.z.plugin.gui.GUIButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SwashbucklerClassGUI extends GUI {
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
        new GUIButton()
                .setMaterial(Material.STICK)
                .setName(Component.text("Testing 1"))
                .addHandler((clickEvent) -> {
                    Bukkit.getLogger().info("Testing 1!");
                })
                .addToGUI(this, 13);
        new GUIButton()
                .setMaterial(Material.STICK)
                .setName(Component.text("Testing 2"))
                .addHandler((clickEvent) -> {
                    Bukkit.getLogger().info("Testing 2!");
                })
                .setCount(2)
                .addToGUI(this, 31);
        new GUIButton()
                .setMaterial(Material.STICK)
                .setName(Component.text("Testing 3"))
                .addHandler((clickEvent) -> {
                    Bukkit.getLogger().info("Testing 3!");
                })
                .setCount(3)
                .addToGUI(this, 49);
    }


}

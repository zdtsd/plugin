package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import io.github.z.plugin.gui.GUIs.ClassGUI;

public class OpenClassGUICommand {

    private final String name = "class_gui";
    public void register(){
        new CommandAPICommand(name)
                .withPermission(CommandPermission.OP)
                .executesPlayer((player, args) -> {new ClassGUI(player, ClassGUI.GUI_SIZE);})
                .register();
    }
}

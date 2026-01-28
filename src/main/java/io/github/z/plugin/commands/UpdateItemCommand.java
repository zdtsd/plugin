package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import io.github.z.plugin.itemstats.ItemStatUtils;
import org.bukkit.entity.Player;

public class UpdateItemCommand {

    private final String name = "refreshStats";
    public void register(){
        new CommandAPICommand(name)
                .withPermission(CommandPermission.OP)
                .executesPlayer((player, args) -> {execute(player);})
                .register();
    }

    private void execute(Player player){
        ItemStatUtils.updateItem(ItemStatUtils.getHeldItem(player));
    }
}

package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.z.plugin.itemstats.AttributeType;
import io.github.z.plugin.itemstats.EnchantmentType;
import io.github.z.plugin.itemstats.ItemStatUtils;
import org.bukkit.entity.Player;

public class SetItemAttributeCommand {

    private final String name = "attr";
    public void register(){
        for(AttributeType stat : AttributeType.values()){
            new CommandAPICommand(name)
                    .withArguments(new LiteralArgument( stat.getName()),
                            new DoubleArgument("value"))
                    .withPermission(CommandPermission.OP)
                    .executesPlayer((player, args) -> {execute(player, args,  stat.getName());})
                    .register();
        }
    }

    private void execute(Player player, CommandArguments args, String statName){
        ItemStatUtils.setItemAttr(ItemStatUtils.getHeldItem(player), statName, (double) args.get("value"));
    }


}

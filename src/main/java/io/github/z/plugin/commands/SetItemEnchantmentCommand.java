package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.z.plugin.itemstats.AttributeType;
import io.github.z.plugin.itemstats.EnchantmentType;
import io.github.z.plugin.itemstats.ItemStatUtils;
import org.bukkit.entity.Player;

public class SetItemEnchantmentCommand {

    private final String name = "ench";
    public void register(){
        for(EnchantmentType stat : EnchantmentType.values()){
            new CommandAPICommand(name)
                    .withArguments(new LiteralArgument(stat.getName()),
                            new DoubleArgument("value"))
                    .withPermission(CommandPermission.OP)
                    .executesPlayer((player, args) -> {execute(player, args, stat.getName());})
                    .register();
        }
    }

    private void execute(Player player, CommandArguments args, String statName){
        ItemStatUtils.setItemEnch(ItemStatUtils.getHeldItem(player), statName, (double) args.get(0));
    }
}

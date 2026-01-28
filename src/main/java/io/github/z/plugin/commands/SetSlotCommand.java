package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.z.plugin.itemstats.AttributeType;
import io.github.z.plugin.itemstats.ItemStatUtils;
import io.github.z.plugin.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class SetSlotCommand {
    private final String name = "setslot";
    public void register(){
        for(EquipmentSlot slot : EquipmentSlot.values()){
            new CommandAPICommand(name)
                    .withArguments(new LiteralArgument(slot.toString()))
                    .withPermission(CommandPermission.OP)
                    .executesPlayer((player, args) -> {execute(player, args, slot);})
                    .register();
        }
    }

    private void execute(Player player, CommandArguments args, EquipmentSlot slot){
        ItemStatUtils.setSlot(slot, ItemStatUtils.getHeldItem(player));
    }
}

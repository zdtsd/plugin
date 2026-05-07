package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import io.github.z.plugin.effects.EffectManager;
import io.github.z.plugin.effects.EffectType;
import org.bukkit.entity.Entity;

import java.util.Collection;

public class EffectCommand {

    private static final String NAME = "eff";
    private static final String ORIGIN = "command";

    public void register() {
        for (EffectType type : EffectType.values()) {
            new CommandAPICommand(NAME)
                    .withArguments(
                            new EntitySelectorArgument.ManyEntities("targets"),
                            new LiteralArgument(type.getName()),
                            new IntegerArgument("strength"),
                            new IntegerArgument("duration"))
                    .withPermission(CommandPermission.OP)
                    .executes((sender, args) -> {
                        Collection<Entity> targets = (Collection<Entity>) args.get("targets");
                        int strength = (int) args.get("strength");
                        int duration = (int) args.get("duration");
                        for (Entity target : targets) {
                            EffectManager.applyEffect(target, type.create(duration, strength), ORIGIN);
                        }
                    })
                    .register();
        }
    }
}

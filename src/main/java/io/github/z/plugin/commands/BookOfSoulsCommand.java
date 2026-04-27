package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI;
import io.github.z.plugin.mobspells.MobSpellManager;
import org.bukkit.entity.EntityType;

import static io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI.soulIdArgument;

public class BookOfSoulsCommand {

    private final String name = "bos";

    public void register() {
        new CommandAPICommand(name)
                .withSubcommand(new CommandAPICommand("nbt")
                        .withSubcommand(new CommandAPICommand("max_health")
                                .withArguments(soulIdArgument("id"))
                                .withArguments(new DoubleArgument("value"))
                                .withPermission(CommandPermission.OP)
                                .executesPlayer((player, args) -> {
                                    LibraryOfSoulsAPI.getInstance().setNBTTag((String) args.get("id"), "max_health", (double) args.get("value"));
                                }))
                        .withSubcommand(new CommandAPICommand("attack_damage")
                                .withArguments(soulIdArgument("id"))
                                .withArguments(new DoubleArgument("value"))
                                .withPermission(CommandPermission.OP)
                                .executesPlayer((player, args) -> {
                                    LibraryOfSoulsAPI.getInstance().setNBTTag((String) args.get("id"), "attack_damage", (double) args.get("value"));
                                }))
                        .withSubcommand(new CommandAPICommand("movement_speed")
                                .withArguments(soulIdArgument("id"))
                                .withArguments(new DoubleArgument("value"))
                                .withPermission(CommandPermission.OP)
                                .executesPlayer((player, args) -> {
                                    LibraryOfSoulsAPI.getInstance().setNBTTag((String) args.get("id"), "movement_speed", (double) args.get("value"));
                                })))
                .withSubcommand(new CommandAPICommand("id")
                        .withArguments(soulIdArgument("id"), new StringArgument("newId"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().setID(player, (String) args.get("id"), (String) args.get("newId"));
                        }))
                .withSubcommand(new CommandAPICommand("type")
                        .withArguments(soulIdArgument("id"), new EntityTypeArgument("entityType"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().setType((String) args.get("id"), (EntityType) args.get("entityType"));
                        }))
                .withSubcommand(new CommandAPICommand("ability")
                        .withArguments(soulIdArgument("id"), new StringArgument("mobSpell")
                                .replaceSuggestions(ArgumentSuggestions.strings(info ->
                                        MobSpellManager.getMobSpellManager().getAllSpells().stream()
                                                .map(d -> d.getName())
                                                .toArray(String[]::new)
                                )), new IntegerArgument("level"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().setAbility((String) args.get("id"), (String) args.get("mobSpell"), (int) args.get("level"));
                        }))
                .register();
    }
}

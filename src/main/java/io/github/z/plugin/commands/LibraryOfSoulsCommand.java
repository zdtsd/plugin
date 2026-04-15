package io.github.z.plugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import io.github.z.plugin.libraryofsouls.LibraryOfSoulsAPI;

public class LibraryOfSoulsCommand {

    private final String name = "los";

    public void register() {
        new CommandAPICommand(name)
                .withSubcommand(new CommandAPICommand("new")
                        .withArguments(LibraryOfSoulsAPI.soulIdArgument("id"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().createSoul(player, (String) args.get("id"));
                        }))
                .withSubcommand(new CommandAPICommand("bos")
                        .withArguments(LibraryOfSoulsAPI.soulIdArgument("id"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().giveBookOfSouls(player, (String) args.get("id"));
                        }))
                .withSubcommand(new CommandAPICommand("delete")
                        .withArguments(LibraryOfSoulsAPI.soulIdArgument("id"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().deleteSoul((String) args.get("id"));
                        }))
                .withSubcommand(new CommandAPICommand("menu")
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((player, args) -> {
                            LibraryOfSoulsAPI.getInstance().openLibraryMenu(player);
                        }))
                .register();
    }
}

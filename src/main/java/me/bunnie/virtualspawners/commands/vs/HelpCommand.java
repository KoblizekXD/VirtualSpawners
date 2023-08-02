/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  org.bukkit.command.CommandSender
 */
package me.bunnie.virtualspawners.commands.vs;

import java.util.ArrayList;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.commands.vs.VSCommand;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand
extends SubCommand {
    private final VirtualSpawners plugin;
    private final VSCommand command;

    public HelpCommand(VirtualSpawners plugin, VSCommand command) {
        this.plugin = plugin;
        this.command = command;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays this helpful menu!";
    }

    @Override
    public String getSyntax() {
        return "/vs help";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vs.commands." + this.getName())) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You lack the permission to use this command!"));
            return;
        }
        ArrayList<String> toSend = new ArrayList<>();
        toSend.add("&7&m--------------&r&7[#fcdfffVirtualSpawners&7]&7&m---------------");
        toSend.add("");
        for (SubCommand cmd : this.command.getSubCommands()) {
            toSend.add(("#bce3f9" + cmd.getSyntax() + " &7- #bce3f9" + cmd.getDescription()));
        }
        toSend.add("");
        toSend.add("&7&m------------------------------------------");
        toSend.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
    }
}
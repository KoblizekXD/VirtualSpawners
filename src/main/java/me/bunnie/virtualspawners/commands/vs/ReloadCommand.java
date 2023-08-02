/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.command.CommandSender
 */
package me.bunnie.virtualspawners.commands.vs;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class ReloadCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public ReloadCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads plugin configuration files!";
    }

    @Override
    public String getSyntax() {
        return "/vs reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vs.commands." + this.getName())) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You lack the permission to use this command!"));
            return;
        }
        this.reload();
        sender.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " Reloaded files!"));
    }

    private void reload() {
        this.plugin.getConfigYML().load();
        this.plugin.getTiersYML().load();
        this.plugin.getBankYML().load();
    }
}
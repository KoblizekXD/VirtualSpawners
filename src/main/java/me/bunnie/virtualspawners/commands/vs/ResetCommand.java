/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.commands.vs;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public ResetCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Resets the money the players spawners has collected!";
    }

    @Override
    public String getSyntax() {
        return "/vs reset <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vs.commands." + this.getName())) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You lack the permission to use this command!"));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You have entered an invalid argument! Refer to &7/vs help &cfor valid usage!"));
            return;
        }
        if (args.length == 2) {
            String targetName = args[1];
            Player target = Bukkit.getPlayerExact((String)targetName);
            if (target == null) {
                sender.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffCould not find the player #a6e4f7" + targetName + "#fcdfff!"));
                return;
            }
            VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)target.getUniqueId());
            profile.getBank().getSpawners().forEach(spawner -> spawner.setValue(0.0));
            profile.save();
            target.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-reset-target").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
            sender.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-reset").replace((CharSequence)"%player%", (CharSequence)profile.getName()).replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
        }
    }
}
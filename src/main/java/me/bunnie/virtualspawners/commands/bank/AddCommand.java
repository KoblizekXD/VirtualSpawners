/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.commands.bank;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AddCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public AddCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Give a player access to your spawnerbank!";
    }

    @Override
    public String getSyntax() {
        return "/spawnerbank add <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length < 2) {
                sender.sendMessage(ChatUtils.format("&cUh Oh! You have entered an invalid argument! Refer to &7/sb help &cfor valid usage!"));
                return;
            }
            if (args.length == 2) {
                VSProfile profile = (VSProfile)VSProfile.getProfiles().get(player.getUniqueId());
                String targetName = args[1];
                OfflinePlayer op = Bukkit.getOfflinePlayer((String)targetName);
                VSProfile targetProfile = new VSProfile(op.getUniqueId());
                if (profile.getUuid().equals(op.getUniqueId())) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou are the owner of the bank!"));
                    return;
                }
                if (profile.getBank().getBankMembers().contains(targetProfile.getUuid())) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou already have #bce3f9" + targetProfile.getName() + " #fcdfffadded to your bank!"));
                    return;
                }
                player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-bank-member-add").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%player%", (CharSequence)String.valueOf(op.getName()))));
                profile.getBank().getBankMembers().add(targetProfile.getUuid());
                profile.save();
                if (!op.isOnline()) {
                    VSProfile.getProfiles().remove(targetProfile.getUuid());
                }
                if (op.isOnline()) {
                    Player target = op.getPlayer();
                    if (target == null) {
                        return;
                    }
                    target.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-bank-access-given").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%player%", (CharSequence)player.getName())));
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
        }
    }
}
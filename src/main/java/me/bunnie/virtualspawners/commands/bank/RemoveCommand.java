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

public class RemoveCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public RemoveCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a player from your spawnerbank!";
    }

    @Override
    public String getSyntax() {
        return "/spawnerbank remove <player>";
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
                VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
                String targetName = args[1];
                OfflinePlayer op = Bukkit.getOfflinePlayer((String)targetName);
                VSProfile targetProfile = new VSProfile(op.getUniqueId());
                if (profile.getUuid().equals((Object)op.getUniqueId())) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou cannot remove your own access!"));
                    return;
                }
                if (!profile.getBank().getBankMembers().contains((Object)targetProfile.getUuid())) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou cannot remove #bce3f9" + targetProfile.getName() + " #fcdfffas they do not have access to your bank!"));
                    return;
                }
                player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-bank-member-remove").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%player%", (CharSequence)String.valueOf((Object)op.getName()))));
                profile.getBank().getBankMembers().remove((Object)targetProfile.getUuid());
                profile.save();
                targetProfile.save();
                if (!op.isOnline()) {
                    VSProfile.getProfiles().remove((Object)targetProfile.getUuid());
                }
                if (op.isOnline()) {
                    Player target = op.getPlayer();
                    if (target == null) {
                        return;
                    }
                    target.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-bank-access-revoke").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%player%", (CharSequence)player.getName())));
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
        }
    }
}
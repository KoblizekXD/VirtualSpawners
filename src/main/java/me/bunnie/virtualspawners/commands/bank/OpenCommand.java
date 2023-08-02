/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
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
import me.bunnie.virtualspawners.ui.player.SpawnerBankMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public OpenCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String getDescription() {
        return "Open another players bank who has given you access!";
    }

    @Override
    public String getSyntax() {
        return "/spawnerbank open <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
            if (args.length < 2) {
                new SpawnerBankMenu(profile.getBank()).getInventory(player);
                return;
            }
            if (args.length == 2) {
                String targetName = args[1];
                OfflinePlayer op = Bukkit.getOfflinePlayer((String)targetName);
                VSProfile targetProfile = new VSProfile(op.getUniqueId());
                if (targetProfile.getBank().getBankMembers().contains((Object)profile.getUuid())) {
                    new SpawnerBankMenu(targetProfile.getBank()).getInventory(player);
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
        }
    }
}
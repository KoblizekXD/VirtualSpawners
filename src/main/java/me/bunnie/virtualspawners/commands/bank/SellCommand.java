/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.commands.bank;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public SellCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getDescription() {
        return "Sells the earnings from all your spawners!";
    }

    @Override
    public String getSyntax() {
        return "/spawnerbank sell";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
            for (Spawner spawner : profile.getBank().getSpawners()) {
                if (spawner.getDrops().isEmpty() || spawner.getDrops() == null) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou dont have anything to sell!"));
                    return;
                }
                this.plugin.getEconomyHook().sell(profile, spawner);
            }
        } else {
            sender.sendMessage("Only players may use this command!");
        }
    }
}
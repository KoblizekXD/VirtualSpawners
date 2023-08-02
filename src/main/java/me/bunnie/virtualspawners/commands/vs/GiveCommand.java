/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.commands.vs;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public GiveCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Gives a player a virtual spawner!";
    }

    @Override
    public String getSyntax() {
        return "/vs give <player> <type> <tier>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vs.commands." + this.getName())) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You lack the permission to use this command!"));
            return;
        }
        if (args.length < 4) {
            sender.sendMessage(ChatUtils.format("&cUh Oh! You have entered an invalid argument! Refer to &7/vs help &cfor valid usage!"));
            return;
        }
        if (args.length == 4) {
            String targetName = args[1];
            String typeName = args[2].toUpperCase();
            Player target = Bukkit.getPlayerExact((String)targetName);
            int tier = Integer.parseInt((String)args[3]);
            String name = null;
            if (target == null) {
                sender.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffCould not find the player #a6e4f7" + targetName + "#fcdfff!"));
                return;
            }
            for (String s : this.plugin.getTiersYML().getConfigurationSection("").getKeys(false)) {
                if (!s.equalsIgnoreCase(typeName)) continue;
                name = s;
            }
            if (name == null) {
                sender.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou have entered an invalid entity!"));
                return;
            }
            if (!this.isTierValid(name, tier)) {
                sender.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffCould not find the tier #a6e4f7" + tier + " #fcdffffor #a6e4f7" + name + " #fcdfffin #a6e4f7tiers.yml#fcdfff!"));
                return;
            }
            if (!this.isEntityValid(name)) {
                sender.sendMessage("1");
                return;
            }
            ItemStack toGive = this.plugin.getSpawnerManager().getNewSpawnerItem(name, tier);
            target.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-receive").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%tier%", (CharSequence)String.valueOf((int)tier)).replace((CharSequence)"%entity%", (CharSequence)name)));
            sender.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-give").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%tier%", (CharSequence)String.valueOf((int)tier)).replace((CharSequence)"%player%", (CharSequence)target.getName()).replace((CharSequence)"%entity%", (CharSequence)name)));
            target.getInventory().addItem(new ItemStack[]{toGive});
        }
    }

    private boolean isEntityValid(String name) {
        return this.plugin.getTiersYML().contains(name);
    }

    private boolean isTierValid(String name, int tier) {
        if (this.plugin.getTiersYML().getConfigurationSection(name + ".tier") == null) {
            return false;
        }
        for (String s : this.plugin.getTiersYML().getConfigurationSection(name + ".tier").getKeys(false)) {
            if (s == null) {
                return false;
            }
            int i = Integer.parseInt((String)s);
            if (i != tier) continue;
            return true;
        }
        return false;
    }
}
/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.commands.vs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.commands.vs.GiveCommand;
import me.bunnie.virtualspawners.commands.vs.HelpCommand;
import me.bunnie.virtualspawners.commands.vs.ReloadCommand;
import me.bunnie.virtualspawners.commands.vs.ResetCommand;
import me.bunnie.virtualspawners.commands.vs.UpgradeCommand;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class VSCommand
implements CommandExecutor,
TabCompleter {
    private final VirtualSpawners plugin;
    private final ArrayList<SubCommand> subCommands;

    public VSCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
        this.subCommands = new ArrayList();
        this.subCommands.add(new GiveCommand(plugin));
        this.subCommands.add(new ResetCommand(plugin));
        this.subCommands.add(new UpgradeCommand(plugin));
        this.subCommands.add(new ReloadCommand(plugin));
        this.subCommands.add(new HelpCommand(plugin, this));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpCommand help = new HelpCommand(this.plugin, this);
            help.execute(sender, args);
        } else {
            boolean validSubCommand = false;
            for (int i = 0; i < this.getSubCommands().size(); ++i) {
                if (!args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) continue;
                validSubCommand = true;
                this.getSubCommands().get(i).execute(sender, args);
                break;
            }
            if (!validSubCommand) {
                sender.sendMessage(ChatUtils.format("&cUh Oh! You have entered an invalid command! Refer to &7/" + label + " help &cfor valid usage!"));
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList toReturn = new ArrayList();
        if (!sender.hasPermission("vs.commands.help")) {
            return toReturn;
        }
        if (args.length == 1) {
            this.subCommands.forEach(arg_0 -> VSCommand.lambda$onTabComplete$0(toReturn, arg_0));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("upgrade")) {
            toReturn.add("looting");
            toReturn.add("tier");
            toReturn.add("efficiency");
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("reset"))) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                toReturn.add(player.getName());
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (String s : this.plugin.getTiersYML().getConfigurationSection("").getKeys(false)) {
                toReturn.add(s);
            }
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            for (EntityType entityType : EntityType.values()) {
                if (!args[2].equalsIgnoreCase(entityType.name()) || !this.plugin.getTiersYML().contains(entityType.name())) continue;
                for (String s : this.plugin.getTiersYML().getConfigurationSection(entityType.name() + ".tier").getKeys(false)) {
                    int i = Integer.parseInt(s);
                    toReturn.add(String.valueOf(i));
                }
            }
        }
        return toReturn;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    private static /* synthetic */ void lambda$onTabComplete$0(List toReturn, SubCommand cmd) {
        toReturn.add(cmd.getName());
    }
}
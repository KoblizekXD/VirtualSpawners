/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.commands.bank;

import java.util.ArrayList;
import java.util.List;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.commands.bank.AddCommand;
import me.bunnie.virtualspawners.commands.bank.HelpCommand;
import me.bunnie.virtualspawners.commands.bank.OpenCommand;
import me.bunnie.virtualspawners.commands.bank.RemoveCommand;
import me.bunnie.virtualspawners.commands.bank.SellCommand;
import me.bunnie.virtualspawners.commands.bank.WithdrawCommand;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.ui.player.SpawnerBankMenu;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SpawnerBankCommand
implements CommandExecutor,
TabCompleter {
    private final VirtualSpawners plugin;
    private final ArrayList<SubCommand> subCommands;

    public SpawnerBankCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
        this.subCommands = new ArrayList();
        this.subCommands.add(new AddCommand(plugin));
        this.subCommands.add(new RemoveCommand(plugin));
        this.subCommands.add(new OpenCommand(plugin));
        this.subCommands.add(new SellCommand(plugin));
        this.subCommands.add(new WithdrawCommand(plugin));
        this.subCommands.add(new HelpCommand(this));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
            new SpawnerBankMenu(profile.getBank()).getInventory(player);
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
        if (args.length == 1) {
            this.subCommands.forEach(arg_0 -> SpawnerBankCommand.lambda$onTabComplete$0((List)toReturn, arg_0));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("withdraw")) {
            for (Spawner spawner : ((VSProfile)VSProfile.getProfiles().get((Object)((Player)sender).getUniqueId())).getBank().getSpawners()) {
                String dropName = this.plugin.getTiersYML().getString(spawner.getName() + ".tier." + spawner.getTier() + ".item-drop.name");
                String drop = ChatUtils.removeColorCodes(dropName).replace((CharSequence)" ", (CharSequence)"-");
                toReturn.add((Object)drop);
            }
        }
        return toReturn;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    private static /* synthetic */ void lambda$onTabComplete$0(List toReturn, SubCommand cmd) {
        toReturn.add((Object)cmd.getName());
    }
}
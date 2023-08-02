/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.commands.bank;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public WithdrawCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public String getDescription() {
        return "Withdraw items from your bank!";
    }

    @Override
    public String getSyntax() {
        return "/spawnerbank withdraw <drop> <amount>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
            Bank bank = profile.getBank();
            if (args.length < 3) {
                sender.sendMessage(ChatUtils.format("&cUh Oh! You have entered an invalid argument! Refer to &7/sb help &cfor valid usage!"));
                return;
            }
            if (args.length == 3) {
                String dropName = args[1];
                for (Spawner spawner : bank.getSpawners()) {
                    if (spawner.getDrops() == null || spawner.getDrops().isEmpty()) {
                        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-withdraw-item-fail").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
                        return;
                    }
                    String drop = ChatUtils.removeColorCodes(spawner.getMobDropName()).replace((CharSequence)" ", (CharSequence)"-");
                    if (!dropName.equalsIgnoreCase(drop)) continue;
                    int amount = Integer.parseInt((String)args[2]);
                    int size = (Integer)spawner.getDrops().get((Object)spawner.getMobDrop());
                    int newAmount = (Integer)spawner.getDrops().get((Object)spawner.getMobDrop()) - amount;
                    if (size == 0 || amount == 0) {
                        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-withdraw-item-fail").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
                        return;
                    }
                    if (amount > size) {
                        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-withdraw-item-limit").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)amount)).replace((CharSequence)"%drop-amount%", (CharSequence)String.valueOf((int)size))));
                        return;
                    }
                    spawner.getDrops().remove((Object)spawner.getMobDrop());
                    spawner.getDrops().put(spawner.getMobDrop(), newAmount);
                    ItemStack itemStack = spawner.getMobDrop();
                    itemStack.setAmount(amount);
                    player.getInventory().addItem(new ItemStack[]{itemStack});
                    player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-withdraw-item").replace((CharSequence)"%drop%", (CharSequence)spawner.getMobDropName()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)amount)).replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
                }
            }
        }
    }
}
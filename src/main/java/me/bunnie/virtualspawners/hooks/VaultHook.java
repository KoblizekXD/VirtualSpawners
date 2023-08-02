/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.UUID
 *  java.util.logging.Level
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package me.bunnie.virtualspawners.hooks;

import java.util.UUID;
import java.util.logging.Level;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private final VirtualSpawners plugin;
    private Economy economy;

    public VaultHook(VirtualSpawners plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().log(Level.SEVERE, "Vault was not found therefore the plugin will not work as intended! Disabling...");
            plugin.getPluginLoader().disablePlugin((Plugin)plugin);
            return;
        }
        RegisteredServiceProvider rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        this.economy = (Economy)rsp.getProvider();
    }

    public void sell(VSProfile profile, Spawner spawner) {
        Player player = Bukkit.getPlayer((UUID)profile.getUuid());
        if (player == null) {
            return;
        }
        double toGive = 0.0;
        if (spawner.getValue() == 0.0 || spawner.getDrops().isEmpty()) {
            player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffYou dont have anything to sell!"));
            return;
        }
        double grandTotal = toGive += spawner.getValue();
        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-sell").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf((double)grandTotal))));
        EconomyResponse response = this.economy.depositPlayer((OfflinePlayer)player, toGive);
        if (response.transactionSuccess()) {
            spawner.setValue(0.0);
        }
    }

    public Economy getEconomy() {
        return this.economy;
    }
}
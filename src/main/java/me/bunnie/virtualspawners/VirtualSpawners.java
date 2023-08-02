/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.function.Consumer
 *  java.util.logging.Level
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.bunnie.virtualspawners;

import me.bunnie.virtualspawners.commands.bank.SpawnerBankCommand;
import me.bunnie.virtualspawners.commands.vs.VSCommand;
import me.bunnie.virtualspawners.database.SQLiteManager;
import me.bunnie.virtualspawners.hooks.VaultHook;
import me.bunnie.virtualspawners.listeners.MenuListener;
import me.bunnie.virtualspawners.listeners.ProfileListener;
import me.bunnie.virtualspawners.listeners.VSListener;
import me.bunnie.virtualspawners.spawner.SpawnerManager;
import me.bunnie.virtualspawners.upgrades.Upgrade;
import me.bunnie.virtualspawners.upgrades.UpgradeManager;
import me.bunnie.virtualspawners.upgrades.bank.CapacityUpgrade;
import me.bunnie.virtualspawners.upgrades.spawner.EfficiencyUpgrade;
import me.bunnie.virtualspawners.upgrades.spawner.LootingUpgrade;
import me.bunnie.virtualspawners.utils.Config;
import me.bunnie.virtualspawners.utils.UpdateUtils;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class VirtualSpawners
extends JavaPlugin {
    private static VirtualSpawners instance;
    private SQLiteManager sqLiteManager;
    private SpawnerManager spawnerManager;
    private UpgradeManager upgradeManager;
    private Config configYML;
    private Config tiersYML;
    private Config bankYML;
    private Config upgradesYML;
    private VaultHook economyHook;

    @Override
    public void onEnable() {
        instance = this;
        new UpdateUtils(this, 107908).getLatestVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.getLogger().log(Level.SEVERE, "You are using an outdated version! (" + this.getDescription().getVersion() + ")");
                this.getLogger().log(Level.SEVERE, "Please update to the newest version for bug patches and newly added features! (" + version + ")");
            }
        });
        this.registerConfigs();
        this.registerManagers();
        this.registerUpgrades();
        this.registerListeners();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        this.sqLiteManager.disconnect();
        instance = null;
    }

    private void registerConfigs() {
        this.configYML = new Config(this, "config", this.getDataFolder().getAbsolutePath());
        this.upgradesYML = new Config(this, "upgrades", this.getDataFolder().getAbsolutePath());
        this.tiersYML = new Config(this, "tiers", this.getDataFolder().getAbsolutePath());
        this.bankYML = new Config(this, "bank", this.getDataFolder().getAbsolutePath());
    }

    private void registerManagers() {
        this.spawnerManager = new SpawnerManager(this);
        this.sqLiteManager = new SQLiteManager(this);
        this.upgradeManager = new UpgradeManager();
        this.economyHook = new VaultHook(this);
        // this.spawnerManager.startSpawn();
    }

    private void registerListeners() {
        Arrays.asList(new Listener[]{new ProfileListener(this), new VSListener(this), new MenuListener()}).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, (Plugin)this));
    }

    private void registerCommands() {
        this.getCommand("vs").setExecutor(new VSCommand(this));
        this.getCommand("vs").setTabCompleter(new VSCommand(this));
        this.getCommand("sb").setExecutor(new SpawnerBankCommand(this));
        this.getCommand("sb").setTabCompleter(new SpawnerBankCommand(this));
    }

    private void registerUpgrades() {
        Arrays.asList((Object[])new Upgrade[]{new CapacityUpgrade(), new EfficiencyUpgrade(), new LootingUpgrade()}).forEach(upgrade -> this.upgradeManager.register((Upgrade)upgrade));
    }

    public String getPrefix() {
        return this.configYML.getString("settings.prefix");
    }

    public static VirtualSpawners getInstance() {
        return instance;
    }

    public SQLiteManager getSqLiteManager() {
        return this.sqLiteManager;
    }

    public SpawnerManager getSpawnerManager() {
        return this.spawnerManager;
    }

    public UpgradeManager getUpgradeManager() {
        return this.upgradeManager;
    }

    public Config getConfigYML() {
        return this.configYML;
    }

    public Config getTiersYML() {
        return this.tiersYML;
    }

    public Config getBankYML() {
        return this.bankYML;
    }

    public Config getUpgradesYML() {
        return this.upgradesYML;
    }

    public VaultHook getEconomyHook() {
        return this.economyHook;
    }
}
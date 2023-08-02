/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.bunnie.virtualspawners.listeners;

import java.util.UUID;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.events.SpawnerRedeemEvent;
import me.bunnie.virtualspawners.events.SpawnerSellEvent;
import me.bunnie.virtualspawners.events.SpawnerSpawnEvent;
import me.bunnie.virtualspawners.events.SpawnerWithdrawEvent;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VSListener
implements Listener {
    private final VirtualSpawners plugin;

    public VSListener(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerRedeem(SpawnerRedeemEvent event) {
        Config config = this.plugin.getConfigYML();
        Player player = event.getPlayer();
        VSProfile profile = (VSProfile)VSProfile.getProfiles().get(player.getUniqueId());
        String name = event.getName();
        Integer tier = event.getTier();
        Integer size = event.getSize();
        Spawner spawner = new Spawner(name);
        spawner.setTier(tier);
        spawner.setSize(size);
        if (!profile.getBank().getSpawners().isEmpty()) {
            for (Spawner profileSpawners : profile.getBank().getSpawners()) {
                if (!this.doSpawnersMatch(profileSpawners, spawner)) continue;
                if (config.getBoolean("settings.title-messages.enabled")) {
                    String redeemTitle = config.getString("title-messages.on-redeem-title");
                    String redeemSubtitle = config.getString("title-messages.on-redeem-subtitle");
                    player.sendTitle(ChatUtils.format(redeemTitle).replace((CharSequence)"%entity%", (CharSequence)spawner.getName()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf(size)), ChatUtils.format(redeemSubtitle), 5, 25, 5);
                }
                player.sendMessage(ChatUtils.format(config.getString("messages.on-redeem").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%entity%", (CharSequence)spawner.getName()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf(size))));
                profileSpawners.setSize(profileSpawners.getSize() + spawner.getSize());
                profile.save();
                return;
            }
        }
        if (config.getBoolean("settings.title-messages.enabled")) {
            String redeemTitle = config.getString("title-messages.on-redeem-title");
            String redeemSubtitle = config.getString("title-messages.on-redeem-subtitle");
            player.sendTitle(ChatUtils.format(redeemTitle).replace((CharSequence)"%entity%", (CharSequence)spawner.getName()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf(size)), ChatUtils.format(redeemSubtitle), 5, 25, 5);
        }
        player.sendMessage(ChatUtils.format(config.getString("messages.on-redeem").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%entity%", (CharSequence)spawner.getName()).replace((CharSequence)"%amount%", (CharSequence)String.valueOf(size))));
        profile.getBank().getSpawners().add(spawner);
        profile.save();
    }

    @EventHandler
    public void onSpawnerWithdraw(SpawnerWithdrawEvent event) {
        Player player = event.getPlayer();
        VSProfile profile = event.getProfile();
        Spawner spawner = event.getSpawner();
        int amount = event.getWithdrawingAmount();
        player.getInventory().addItem(new ItemStack[]{this.plugin.getSpawnerManager().getItemFromSpawner(spawner, amount)});
        if (amount == spawner.getSize()) {
            profile.getBank().getSpawners().remove(spawner);
        } else {
            spawner.setSize(spawner.getSize() - amount);
        }
        profile.save();
        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-withdraw-spawner").replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)amount)).replace((CharSequence)"%entity%", (CharSequence)spawner.getName()).replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        Spawner spawner = event.getSpawner();
        int spawned = 0;
        spawner.setSpawned(spawner.getSpawned() + (spawned += spawner.getSize()));
        spawner.setAliveMobs(spawner.getAliveMobs() + spawned);
        this.onKill(spawner, spawner.getLevel("Efficiency"));
    }

    @EventHandler
    public void onSpawnerSell(SpawnerSellEvent event) {
        VSProfile profile = event.getProfile();
        Player player = Bukkit.getPlayer((UUID)profile.getUuid());
        if (player == null) {
            return;
        }
        Spawner spawner = event.getSpawner();
        this.plugin.getEconomyHook().sell(profile, spawner);
    }

    private void onKill(final Spawner spawner, final int amount) {
        long interval = 700L;
        new BukkitRunnable(){

            public void run() {
                if (spawner.getAliveMobs() != 0) {
                    int mobsToKill = Math.min((int)spawner.getAliveMobs(), (int)amount);
                    spawner.setAliveMobs(spawner.getAliveMobs() - mobsToKill);
                    VSListener.this.getLoot(spawner, mobsToKill * spawner.getLevel("Looting"));
                    if (VSListener.this.plugin.getConfigYML().getBoolean("settings.looting-multiply-xp")) {
                        spawner.setCollectedXP(mobsToKill * spawner.getLevel("Looting"));
                        return;
                    }
                    spawner.setCollectedXP(mobsToKill);
                }
                this.cancel();
            }
        }.runTaskTimerAsynchronously((Plugin)this.plugin, interval, interval);
    }

    private void getLoot(Spawner spawner, int amount) {
        if (!spawner.getDrops().isEmpty()) {
            spawner.getDrops().put(spawner.getMobDrop(), (spawner.getDrops().get(spawner.getMobDrop()) + amount));
            return;
        }
        spawner.getDrops().put(spawner.getMobDrop(), amount);
    }

    private boolean doSpawnersMatch(Spawner a, Spawner b) {
        return a.getName().equals(b.getName()) && a.getTier() == b.getTier();
    }
}
/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.bunnie.virtualspawners.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.events.SpawnerSpawnEvent;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerManager {
    private final VirtualSpawners plugin;

    public SpawnerManager(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    public ItemStack getNewSpawnerItem(String name, int tier) {
        if (tier == 0) {
            tier = 1;
        }
        String spawnerName = this.plugin.getConfigYML().getString("settings.redeemable-spawner.name").replace("%name%", ChatUtils.fixCapitalisation(name.toLowerCase())).replace("%tier%", String.valueOf(tier));
        List<String> lore = this.plugin.getConfigYML().getStringList("settings.redeemable-spawner.lore");
        ArrayList<String> toReturn = new ArrayList();
        for (String s : lore) {
            s = s.replace("%entity%", ChatUtils.fixCapitalisation(name.toLowerCase()));
            s = s.replace("%drop%", this.plugin.getTiersYML().getString(name + ".tier." + tier + ".item-drop.name"));
            s = s.replace("%tier%", String.valueOf(tier));
            toReturn.add(s);
        }
        ItemBuilder builder = new ItemBuilder(Material.valueOf(this.plugin.getConfigYML().getString("settings.redeemable-spawner.material")));
        if (builder.getMaterial().equals(Material.PLAYER_HEAD)) {
            builder.setHead(this.plugin.getConfigYML().getString("settings.redeemable-spawner.skull-texture"));
        }
        return builder.setName(ChatUtils.format(spawnerName)).setLore((ArrayList<String>) ChatUtils.format(toReturn)).addPDC("entity", PersistentDataType.STRING, name).addPDC("tier", PersistentDataType.INTEGER, tier).addPDC("size", PersistentDataType.INTEGER, 1).setCustomModelData(60).build();
    }

    public ItemStack getItemFromSpawner(Spawner spawner, int amount) {
        String name = this.plugin.getConfigYML().getString("settings.redeemable-spawner.name").replace("%name%", ChatUtils.fixCapitalisation(spawner.getName().toLowerCase())).replace("%tier%", String.valueOf(spawner.getTier()));
        List<String> lore = this.plugin.getConfigYML().getStringList("settings.redeemable-spawner.lore");
        ArrayList<String> toReturn = new ArrayList();
        for (String s : lore) {
            s = s.replace("%entity%", ChatUtils.fixCapitalisation(spawner.getName().toLowerCase()));
            s = s.replace("%drop%", spawner.getMobDropName());
            s = s.replace("%tier%", String.valueOf(spawner.getTier()));
            toReturn.add(s);
        }
        ItemBuilder builder = new ItemBuilder(Material.valueOf(this.plugin.getConfigYML().getString("settings.redeemable-spawner.material")));
        if (builder.getMaterial().equals(Material.PLAYER_HEAD)) {
            builder.setHead(this.plugin.getConfigYML().getString("settings.redeemable-spawner.skull-texture"));
        }
        return builder.setName(ChatUtils.format(name)).setLore((ArrayList<String>) ChatUtils.format(toReturn)).addPDC("entity", PersistentDataType.STRING, spawner.getName()).addPDC("tier", PersistentDataType.INTEGER, spawner.getTier()).addPDC("size", PersistentDataType.INTEGER, amount).build();
    }

    public void startSpawn() {
        long interval = 20L * (long)this.plugin.getConfigYML().getInt("settings.spawner-interval");
        new BukkitRunnable(){

            public void run() {
                if (VSProfile.getProfiles().isEmpty()) {
                    return;
                }
                for (VSProfile profile : VSProfile.getProfiles().values()) {
                    if (profile.getBank().getSpawners().isEmpty()) continue;
                    for (Spawner spawner : profile.getBank().getSpawners()) {
                        Player player = Bukkit.getPlayer(profile.getUuid());
                        if (player == null) {
                            return;
                        }
                        if (!SpawnerManager.this.isWorldValid(player.getWorld())) {
                            return;
                        }
                        SpawnerManager.this.plugin.getServer().getPluginManager().callEvent(new SpawnerSpawnEvent(player, spawner));
                    }
                }
            }
        }.runTaskTimer(this.plugin, interval, interval);
    }

    public boolean isWorldValid(World world) {
        for (String s : this.plugin.getConfigYML().getStringList("settings.disabled-worlds")) {
            World disabledWorld = Bukkit.getWorld(s);
            if (disabledWorld == null) {
                return true;
            }
            if (world != disabledWorld) continue;
            return false;
        }
        return true;
    }
}
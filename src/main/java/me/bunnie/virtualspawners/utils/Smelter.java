package me.bunnie.virtualspawners.utils;

import com.google.common.collect.Queues;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.events.SpawnerSpawnEvent;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.spawner.SpawnerManager;
import me.bunnie.virtualspawners.ui.player.FuelSystemMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Smelter {
    private final Player player;
    private final VirtualSpawners plugin;
    private List<ItemStack> queue;
    private BukkitTask task;

    public Smelter(Player player) {
        this.player = player;
        this.queue = new ArrayList<>();
        this.plugin = VirtualSpawners.getInstance();
        startSpawnForPlayerFor(player);
    }
    public static Smelter getOrCreateSmelter(Player player) {
        if (FuelSystemMenu.SMELTINGS.containsKey(player))
            return FuelSystemMenu.SMELTINGS.get(player);
        Smelter smelter = new Smelter(player);
        FuelSystemMenu.SMELTINGS.put(player, new Smelter(player));
        return smelter;
    }
    public static void destroySmelter(Player player) {
        FuelSystemMenu.SMELTINGS.get(player).task.cancel();
        FuelSystemMenu.SMELTINGS.remove(player);
    }
    public static boolean canDestroySmelter(Player player) {
        return FuelSystemMenu.SMELTINGS.get(player).queue.isEmpty();
    }
    public void addMaterial(ItemStack stack) {
        if (queue.size() < 9)
            queue.add(stack);
    }
    public void startSpawnForPlayerFor(Player player) {
        var spawnerManager = VirtualSpawners.getInstance().getSpawnerManager();
        long interval = 20L; // * this.plugin.getConfigYML().getInt("settings.spawner-interval");
        task = new BukkitRunnable(){
            public void run() {
                if (VSProfile.getProfiles().isEmpty()) {
                    return;
                }
                VSProfile profile = VSProfile.getProfiles().get(player.getUniqueId());
                if (!profile.getBank().getSpawners().isEmpty() && !queue.isEmpty()) {
                        queue.get(0).setAmount(queue.get(0).getAmount()-1);
                        if (queue.get(0).getAmount() != 0) {
                            for (Spawner spawner : profile.getBank().getSpawners()) {
                                if (!spawnerManager.isWorldValid(player.getWorld())) {
                                    return;
                                }
                                plugin.getServer().getPluginManager().callEvent(new SpawnerSpawnEvent(player, spawner));
                            }
                        } else {
                            queue.remove(0);
                            destroySmelter(player);
                        }


                }
            }
        }.runTaskTimer(this.plugin, interval, interval);
    }

    public List<ItemStack> getQueue() {
        return queue;
    }
}

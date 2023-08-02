/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.net.URL
 *  java.util.Scanner
 *  java.util.function.Consumer
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.bunnie.virtualspawners.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import me.bunnie.virtualspawners.VirtualSpawners;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateUtils {
    private final VirtualSpawners plugin;
    private final int resourceId;

    public UpdateUtils(VirtualSpawners plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getLatestVersion(final Consumer<String> consumer) {
        new BukkitRunnable(){

            public void run() {
                try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + UpdateUtils.this.resourceId).openStream();
                     Scanner scanner = new Scanner(inputStream);){
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                } catch (IOException exception) {
                    UpdateUtils.this.plugin.getLogger().info("Unable to get updates: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously((Plugin)this.plugin);
    }
}
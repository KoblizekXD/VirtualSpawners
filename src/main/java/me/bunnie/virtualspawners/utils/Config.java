/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.bunnie.virtualspawners.utils;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config
extends YamlConfiguration {
    private final File file;
    private String name;
    private String directory;

    public Config(JavaPlugin plugin, String name, String directory) {
        this.setName(name);
        this.setDirectory(directory);
        this.file = new File(directory, name + ".yml");
        if (!this.file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        this.load();
        this.save();
    }

    public void load() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException ex2) {
            Exception e = new Exception();
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getDirectory() {
        return this.directory;
    }
}
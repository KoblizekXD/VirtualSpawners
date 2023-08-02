/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.upgrades;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Upgrade {
    private int level = 1;

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getPrice(int var1);

    public abstract int getStartingLevel();

    public abstract int getMaxLevel();

    public abstract Type getType();

    public abstract int getMenuSlot();

    public abstract ItemStack getIcon();

    public abstract void execute(Player var1);

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static enum Type {
        BANK,
        SPAWNER;

    }
}
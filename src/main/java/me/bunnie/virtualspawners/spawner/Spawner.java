/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.spawner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.utils.Config;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Spawner {
    private Map<ItemStack, Integer> drops;
    private String name;
    private int tier;
    private int size;
    private int aliveMobs;
    private int spawned;
    private int collectedXP;
    private double value;

    public Spawner(String name) {
        this.name = name;
        this.drops = new HashMap();
    }

    public String getName() {
        return this.name.toUpperCase();
    }

    public ItemStack getMobDrop() {
        Config config = VirtualSpawners.getInstance().getTiersYML();
        Material material = Material.valueOf(config.getString(this.name + ".tier." + this.tier + ".item-drop.material"));
        String dropName = config.getString(this.name + ".tier." + this.tier + ".item-drop.name");
        List lore = config.getStringList(this.name + ".tier." + this.tier + ".item-drop.lore");
        boolean enchanted = config.getBoolean(this.name + ".tier." + this.tier + ".item-drop.enchanted");
        return new ItemBuilder(material).setName(dropName).setLore((ArrayList<String>) lore).setGlow(enchanted).build();
    }

    public String getMobDropName() {
        return VirtualSpawners.getInstance().getTiersYML().getString(this.name + ".tier." + this.tier + ".item-drop.name");
    }

    public double getPrice() {
        return VirtualSpawners.getInstance().getTiersYML().getDouble(this.name + ".tier." + this.tier + ".item-drop.price");
    }

    public double getValue() {
        this.value = (double) this.drops.get(this.getMobDrop()).intValue() * this.getPrice();
        return this.value;
    }

    public int getXP() {
        return VirtualSpawners.getInstance().getTiersYML().getInt(this.name + ".tier." + this.tier + ".item-drop.xp");
    }

    public int getMaxTier() {
        for (String s : VirtualSpawners.getInstance().getTiersYML().getConfigurationSection(this.name + ".tier").getKeys(false)) {
            int i = Integer.parseInt(s);
            if (i != this.tier) continue;
            return i;
        }
        return 1;
    }

    public void setValue(double value) {
        this.value = value;
        this.drops.remove(this.getMobDrop());
        this.drops.put(this.getMobDrop(), 0);
    }

    public int setCollectedXP(int killedMobs) {
        this.collectedXP += killedMobs * this.getXP();
        return this.collectedXP;
    }

    public int getLevel(String upgradeName) {
        return VirtualSpawners.getInstance().getUpgradeManager().getUpgrade(upgradeName).getLevel();
    }

    public int getTier() {
        return this.tier;
    }

    public int getSize() {
        return this.size;
    }

    public int getAliveMobs() {
        return this.aliveMobs;
    }

    public int getSpawned() {
        return this.spawned;
    }

    public int getCollectedXP() {
        return this.collectedXP;
    }

    public void setDrops(Map<ItemStack, Integer> drops) {
        this.drops = drops;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setAliveMobs(int aliveMobs) {
        this.aliveMobs = aliveMobs;
    }

    public void setSpawned(int spawned) {
        this.spawned = spawned;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Spawner)) {
            return false;
        }
        Spawner other = (Spawner)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTier() != other.getTier()) {
            return false;
        }
        if (this.getSize() != other.getSize()) {
            return false;
        }
        if (this.getAliveMobs() != other.getAliveMobs()) {
            return false;
        }
        if (this.getSpawned() != other.getSpawned()) {
            return false;
        }
        if (this.getCollectedXP() != other.getCollectedXP()) {
            return false;
        }
        if (Double.compare(this.getValue(), other.getValue()) != 0) {
            return false;
        }
        Map<ItemStack, Integer> this$drops = this.getDrops();
        Map<ItemStack, Integer> other$drops = other.getDrops();
        if (this$drops == null ? other$drops != null : !this$drops.equals(other$drops)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Spawner;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTier();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getAliveMobs();
        result = result * 59 + this.getSpawned();
        result = result * 59 + this.getCollectedXP();
        long $value = Double.doubleToLongBits(this.getValue());
        result = result * 59 + (int)($value >>> 32 ^ $value);
        Map<ItemStack, Integer> $drops = this.getDrops();
        result = result * 59 + ($drops == null ? 43 : $drops.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Spawner(drops=" + this.getDrops() + ", name=" + this.getName() + ", tier=" + this.getTier() + ", size=" + this.getSize() + ", aliveMobs=" + this.getAliveMobs() + ", spawned=" + this.getSpawned() + ", collectedXP=" + this.getCollectedXP() + ", value=" + this.getValue() + ")";
    }

    public Map<ItemStack, Integer> getDrops() {
        return this.drops;
    }
}
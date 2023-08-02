/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.UUID
 */
package me.bunnie.virtualspawners.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.upgrades.Upgrade;

public class Bank {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final VSProfile profile;
    private List<Upgrade> bankUpgrades;
    private List<Upgrade> spawnerUpgrades;
    private List<Spawner> spawners;
    private List<UUID> bankMembers;

    public Bank(VSProfile profile) {
        this.profile = profile;
        this.spawners = new ArrayList();
        this.bankUpgrades = new ArrayList();
        this.spawnerUpgrades = new ArrayList();
        this.bankMembers = new ArrayList();
        this.defaultUpgrades();
    }

    public int getTotalSpawners() {
        int toReturn = 0;
        for (Spawner spawner : this.spawners) {
            toReturn += spawner.getSize();
        }
        return toReturn;
    }

    private void defaultUpgrades() {
        String[] upgradeNames;
        for (String upgradeName : upgradeNames = new String[]{"Capacity", "Looting", "Efficiency"}) {
            Upgrade upgrade = this.plugin.getUpgradeManager().getUpgrade(upgradeName);
            upgrade.setLevel(1);
            if (upgradeName.equals("Capacity")) {
                this.bankUpgrades.add(upgrade);
                continue;
            }
            this.spawnerUpgrades.add(upgrade);
        }
    }

    public int getCurrentSize() {
        Upgrade upgrade = this.plugin.getUpgradeManager().getUpgrade("Capacity");
        return this.plugin.getBankYML().getInt("upgrades.capacity.levels." + upgrade.getLevel() + ".upgraded-capacity");
    }

    public VirtualSpawners getPlugin() {
        return this.plugin;
    }

    public VSProfile getProfile() {
        return this.profile;
    }

    public List<Upgrade> getBankUpgrades() {
        return this.bankUpgrades;
    }

    public List<Upgrade> getSpawnerUpgrades() {
        return this.spawnerUpgrades;
    }

    public List<Spawner> getSpawners() {
        return this.spawners;
    }

    public List<UUID> getBankMembers() {
        return this.bankMembers;
    }

    public void setBankUpgrades(List<Upgrade> bankUpgrades) {
        this.bankUpgrades = bankUpgrades;
    }

    public void setSpawnerUpgrades(List<Upgrade> spawnerUpgrades) {
        this.spawnerUpgrades = spawnerUpgrades;
    }

    public void setSpawners(List<Spawner> spawners) {
        this.spawners = spawners;
    }

    public void setBankMembers(List<UUID> bankMembers) {
        this.bankMembers = bankMembers;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        Bank other = (Bank)o;
        if (!other.canEqual(this)) {
            return false;
        }
        VirtualSpawners this$plugin = this.getPlugin();
        VirtualSpawners other$plugin = other.getPlugin();
        if (this$plugin == null ? other$plugin != null : !this$plugin.equals((Object)other$plugin)) {
            return false;
        }
        VSProfile this$profile = this.getProfile();
        VSProfile other$profile = other.getProfile();
        if (this$profile == null ? other$profile != null : !this$profile.equals(other$profile)) {
            return false;
        }
        List<Upgrade> this$bankUpgrades = this.getBankUpgrades();
        List<Upgrade> other$bankUpgrades = other.getBankUpgrades();
        if (this$bankUpgrades == null ? other$bankUpgrades != null : !this$bankUpgrades.equals(other$bankUpgrades)) {
            return false;
        }
        List<Upgrade> this$spawnerUpgrades = this.getSpawnerUpgrades();
        List<Upgrade> other$spawnerUpgrades = other.getSpawnerUpgrades();
        if (this$spawnerUpgrades == null ? other$spawnerUpgrades != null : !this$spawnerUpgrades.equals(other$spawnerUpgrades)) {
            return false;
        }
        List<Spawner> this$spawners = this.getSpawners();
        List<Spawner> other$spawners = other.getSpawners();
        if (this$spawners == null ? other$spawners != null : !this$spawners.equals(other$spawners)) {
            return false;
        }
        List<UUID> this$bankMembers = this.getBankMembers();
        List<UUID> other$bankMembers = other.getBankMembers();
        return !(this$bankMembers == null ? other$bankMembers != null : !this$bankMembers.equals(other$bankMembers));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Bank;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        VirtualSpawners $plugin = this.getPlugin();
        result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
        VSProfile $profile = this.getProfile();
        result = result * 59 + ($profile == null ? 43 : $profile.hashCode());
        List<Upgrade> $bankUpgrades = this.getBankUpgrades();
        result = result * 59 + ($bankUpgrades == null ? 43 : $bankUpgrades.hashCode());
        List<Upgrade> $spawnerUpgrades = this.getSpawnerUpgrades();
        result = result * 59 + ($spawnerUpgrades == null ? 43 : $spawnerUpgrades.hashCode());
        List<Spawner> $spawners = this.getSpawners();
        result = result * 59 + ($spawners == null ? 43 : $spawners.hashCode());
        List<UUID> $bankMembers = this.getBankMembers();
        result = result * 59 + ($bankMembers == null ? 43 : $bankMembers.hashCode());
        return result;
    }

    public String toString() {
        return "Bank(plugin=" + this.getPlugin() + ", profile=" + this.getProfile() + ", bankUpgrades=" + this.getBankUpgrades() + ", spawnerUpgrades=" + this.getSpawnerUpgrades() + ", spawners=" + this.getSpawners() + ", bankMembers=" + this.getBankMembers() + ")";
    }
}
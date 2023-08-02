/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 *
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.sql.Connection
 *  java.sql.ResultSet
 *  java.sql.SQLException
 *  java.sql.Statement
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Bukkit
 */
package me.bunnie.virtualspawners.profile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.upgrades.Upgrade;
import org.bukkit.Bukkit;

public class VSProfile {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private static final Map<UUID, VSProfile> profiles = new HashMap();
    private final UUID uuid;
    private Bank bank;

    public VSProfile(UUID uuid) {
        this.uuid = uuid;
        this.bank = new Bank(this);
        this.addToCache();
        this.load();
    }

    private void create() {
        Connection connection = this.plugin.getSqLiteManager().getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO profiles (UUID,SPAWNERS,SPAWNER_UPGRADES,BANK_UPGRADES,BANK_MEMBERS) VALUES ('" + this.uuid.toString() + "','" + this.bank.getSpawners() + "','" + this.bank.getSpawnerUpgrades() + "','" + this.bank.getBankUpgrades() + "','" + this.bank.getBankMembers() + "')");
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void load() {
        Connection connection = this.plugin.getSqLiteManager().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM profiles WHERE UUID='" + this.uuid.toString() + "'");
            if (rs.next()) {
                String[] bankInfo;
                String bankString;
                String[] memberArray;
                String[] spawnerArray;
                String spawnerString = rs.getString("SPAWNERS").trim();
                for (String s : spawnerArray = spawnerString.split(",")) {
                    String[] spawnerInfo = s.split(":");
                    if (spawnerInfo[0].isBlank() || spawnerInfo[0].equalsIgnoreCase("[]")) continue;
                    String name = spawnerInfo[0].trim();
                    int tier = Integer.parseInt((String) spawnerInfo[1].trim());
                    int size = Integer.parseInt((String) spawnerInfo[2].trim());
                    Spawner spawner = new Spawner(name);
                    spawner.setTier(tier);
                    spawner.setSize(size);
                    this.bank.getSpawners().add(spawner);
                    for (int i = 3; i < spawnerInfo.length; ++i) {
                        String[] upgradeInfo = spawnerInfo[i].split("-");
                        if (upgradeInfo.length != 2) continue;
                        String upgradeName = upgradeInfo[0];
                        int upgradeLevel = Integer.parseInt((String) upgradeInfo[1]);
                        this.applyUpgrades(upgradeName, upgradeLevel, Upgrade.Type.SPAWNER);
                    }
                }
                String memberString = rs.getString("BANK_MEMBERS").trim();
                for (String s : memberArray = memberString.split(",")) {
                    if (s.isBlank() || s.equalsIgnoreCase("[]")) continue;
                    s = s.replace((CharSequence) ",", (CharSequence) "").trim();
                    UUID uuid = UUID.fromString((String) s);
                    this.bank.getBankMembers().add(uuid);
                }
                if (rs.getString("BANK_UPGRADES") != null) {
                    String[] bankArray;
                    bankString = rs.getString("BANK_UPGRADES").trim();
                    for (String s : bankArray = bankString.split(",")) {
                        bankInfo = s.split(":");
                        if (bankInfo[0].isBlank() || bankInfo[0].equalsIgnoreCase("[]") || bankInfo.length != 2)
                            continue;
                        String upgradeName = bankInfo[0];
                        int level = Integer.parseInt(bankInfo[1]);
                        this.applyUpgrades(upgradeName, level, Upgrade.Type.BANK);
                    }
                }
                if (rs.getString("SPAWNER_UPGRADES") != null) {
                    String[] bankArray;
                    bankString = rs.getString("SPAWNER_UPGRADES").trim();
                    for (String s : bankArray = bankString.split(",")) {
                        bankInfo = s.split("-");
                        if (bankInfo[0].isBlank() || bankInfo[0].equalsIgnoreCase("[]") || bankInfo.length != 2)
                            continue;
                        String upgradeName = bankInfo[0].trim();
                        int level = Integer.parseInt(bankInfo[1]);
                        this.applyUpgrades(upgradeName, level, Upgrade.Type.SPAWNER);
                    }
                }
                statement.close();
            } else {
                this.create();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void save() {
        Connection connection = this.plugin.getSqLiteManager().getConnection();
        try {
            Statement statement = connection.createStatement();
            String bu = " ";
            StringBuilder s = new StringBuilder();
            StringBuilder b = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (Spawner spawner : this.bank.getSpawners()) {
                s.append(spawner.getName()).append(":").append(spawner.getTier()).append(":").append(spawner.getSize());
                s.append(",");
            }
            for (Upgrade upgrade : this.bank.getBankUpgrades()) {
                b.append(upgrade.getName()).append("-").append(upgrade.getLevel());
                b.append(",");
            }
            for (Upgrade upgrade : this.bank.getSpawnerUpgrades()) {
                sb.append(upgrade.getName()).append("-").append(upgrade.getLevel());
                sb.append(",");
            }
            for (UUID uuid : this.bank.getBankMembers()) {
                bu = uuid.toString() + "," + bu;
            }
            statement.executeUpdate("UPDATE profiles SET SPAWNERS='" + s.toString().trim() + "', BANK_MEMBERS='" + bu + "', SPAWNER_UPGRADES='" + sb.toString().trim() + "', BANK_UPGRADES='" + b.toString().trim() + "' WHERE UUID='" + this.uuid.toString() + "'");
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getName() {
        return Bukkit.getOfflinePlayer((UUID) this.uuid).getName();
    }

    private void addToCache() {
        if (profiles.containsKey(this.uuid)) {
            return;
        }
        profiles.put(this.uuid, this);
    }

    private void applyUpgrades(String upgradeName, int level, Upgrade.Type type) {
        Upgrade upgrade = this.plugin.getUpgradeManager().getUpgrade(upgradeName);
        upgrade.setLevel(level);
        this.bank.getBankUpgrades().remove((Object) upgrade);
        this.bank.getSpawnerUpgrades().remove((Object) upgrade);
        if (type.equals(Upgrade.Type.BANK)) {
            this.bank.getBankUpgrades().add(upgrade);
        } else if (type.equals(Upgrade.Type.SPAWNER)) {
            this.bank.getSpawnerUpgrades().add(upgrade);
        }
    }

    public VirtualSpawners getPlugin() {
        return this.plugin;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VSProfile)) {
            return false;
        }
        VSProfile other = (VSProfile) o;
        if (!other.canEqual(this)) {
            return false;
        }
        VirtualSpawners this$plugin = this.getPlugin();
        VirtualSpawners other$plugin = other.getPlugin();
        if (this$plugin == null ? other$plugin != null : !this$plugin.equals((Object) other$plugin)) {
            return false;
        }
        UUID this$uuid = this.getUuid();
        UUID other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals((Object) other$uuid)) {
            return false;
        }
        Bank this$bank = this.getBank();
        Bank other$bank = other.getBank();
        return !(this$bank == null ? other$bank != null : !this$bank.equals(other$bank));
    }

    protected boolean canEqual(Object other) {
        return other instanceof VSProfile;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        VirtualSpawners $plugin = this.getPlugin();
        result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
        UUID $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
        Bank $bank = this.getBank();
        result = result * 59 + ($bank == null ? 43 : $bank.hashCode());
        return result;
    }

    public String toString() {
        return "VSProfile(plugin=" + this.getPlugin() + ", uuid=" + this.getUuid() + ", bank=" + this.getBank() + ")";
    }

    public static Map<UUID, VSProfile> getProfiles() {
        return profiles;
    }
}
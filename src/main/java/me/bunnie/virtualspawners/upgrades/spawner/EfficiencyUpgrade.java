/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.upgrades.spawner;

import java.util.ArrayList;
import java.util.List;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.upgrades.Upgrade;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EfficiencyUpgrade
extends Upgrade {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();

    @Override
    public String getName() {
        return "Efficiency";
    }

    @Override
    public String getDescription() {
        return "Upgrade your Spawners Efficiency!";
    }

    @Override
    public int getPrice(int level) {
        return this.plugin.getUpgradesYML().getInt("upgrades.spawner.efficiency.price");
    }

    @Override
    public int getStartingLevel() {
        return this.plugin.getUpgradesYML().getInt("settings.spawner.default-efficiency");
    }

    @Override
    public int getMaxLevel() {
        return this.plugin.getUpgradesYML().getInt("settings.spawner.max-efficiency");
    }

    @Override
    public Upgrade.Type getType() {
        return Upgrade.Type.SPAWNER;
    }

    @Override
    public int getMenuSlot() {
        return this.plugin.getBankYML().getInt("menus.spawner-upgrade.efficiency.slot");
    }

    @Override
    public ItemStack getIcon() {
        Economy economy = this.plugin.getEconomyHook().getEconomy();
        double toScaleBy = this.plugin.getUpgradesYML().getDouble("upgrades.spawner.efficiency.price-scale");
        int nextLevel = this.getLevel() + 1;
        int finalPrice = (int)Math.round((double)this.getPrice(nextLevel) * Math.pow(toScaleBy, nextLevel - 1));
        String formattedPrice = economy.format(finalPrice);
        List<String> lore = this.plugin.getBankYML().getStringList("menus.spawner-upgrade.efficiency.lore");
        ArrayList<String> toReturn = new ArrayList<>();
        for (String s : lore) {
            s = s.replace("%current-level%", String.valueOf(this.getLevel()));
            if (this.getLevel() == this.getMaxLevel()) {
                s = s.replace("%cost%", "$0");
                s = s.replace("%next-level%", this.plugin.getBankYML().getString("menus.spawner-upgrade.replace.max-level").replace("%current-level%", String.valueOf(this.getLevel())).replace("%max-level%", String.valueOf(this.getMaxLevel())));
            } else {
                s = s.replace("%cost%", formattedPrice);
                s = s.replace("%next-level%", String.valueOf(nextLevel));
            }
            toReturn.add(s);
        }
        return new ItemBuilder(Material.valueOf(this.plugin.getBankYML().getString("menus.spawner-upgrade.efficiency.material"))).setName(this.plugin.getBankYML().getString("menus.spawner-upgrade.efficiency.name")).setLore(toReturn).setGlow(true).setCustomModelData(this.plugin.getBankYML().getInt("menus.spawner-upgrade.efficiency.custom-model-data")).build();
    }

    @Override
    public void execute(Player player) {
        VSProfile profile = VSProfile.getProfiles().get(player.getUniqueId());
        Bank bank = profile.getBank();
        Economy economy = this.plugin.getEconomyHook().getEconomy();
        int nextLevel = this.getLevel() + 1;
        economy.withdrawPlayer(player, this.getPrice(nextLevel));
        this.setLevel(nextLevel);
        bank.getSpawnerUpgrades().remove(this);
        bank.getSpawnerUpgrades().add(this);
        profile.save();
        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-upgrade").replace("%prefix%", this.plugin.getPrefix()).replace("%upgrade%", this.getName().toLowerCase()).replace("%level%", String.valueOf(this.getLevel()))));
    }
}
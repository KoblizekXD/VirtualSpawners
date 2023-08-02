/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.upgrades.bank;

import java.util.ArrayList;
import java.util.List;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.upgrades.Upgrade;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CapacityUpgrade
extends Upgrade {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();

    @Override
    public String getName() {
        return "Capacity";
    }

    @Override
    public String getDescription() {
        return "Upgrade your Bank spawner capacity!";
    }

    @Override
    public int getPrice(int level) {
        return this.plugin.getUpgradesYML().getInt("upgrades.bank.capacity.levels." + level + ".price");
    }

    @Override
    public int getStartingLevel() {
        return this.plugin.getUpgradesYML().getInt("settings.bank.default-capacity");
    }

    @Override
    public int getMaxLevel() {
        ArrayList toReturn = new ArrayList();
        for (String s : this.plugin.getUpgradesYML().getConfigurationSection("upgrades.bank.capacity.levels").getKeys(false)) {
            if (s == null) {
                return 0;
            }
            Integer i = Integer.parseInt((String)s);
            toReturn.add((Object)i);
        }
        return (Integer)toReturn.get(toReturn.size() - 1);
    }

    @Override
    public Upgrade.Type getType() {
        return Upgrade.Type.BANK;
    }

    @Override
    public int getMenuSlot() {
        return this.plugin.getBankYML().getInt("menus.bank-upgrade.capacity.slot");
    }

    @Override
    public ItemStack getIcon() {
        List<String> lore = this.plugin.getBankYML().getStringList("menus.bank-upgrade.capacity.lore");
        ArrayList<String> toReturn = new ArrayList<>();
        int nextLevel = this.getLevel() + 1;
        String formattedPrice = this.plugin.getEconomyHook().getEconomy().format((double)this.getPrice(nextLevel));
        for (String s : lore) {
            s = s.replace("%current-level%", String.valueOf((int)this.getLevel()));
            if (this.getName().equalsIgnoreCase("capacity")) {
                s = s.replace("%current-size%", String.valueOf((int)this.getCurrentSize()));
            }
            if (this.getLevel() == this.getMaxLevel()) {
                s = s.replace("%cost%", "$0");
                s = s.replace("%next-level%", this.plugin.getBankYML().getString("menus.bank-upgrade.replace.max-level").replace("%current-level%", String.valueOf((int)this.getLevel())).replace("%max-level%", String.valueOf((int)this.getMaxLevel())));
                if (this.getName().equalsIgnoreCase("capacity")) {
                    s = s.replace("%next-size%", String.valueOf((int)this.getCurrentSize()));
                }
            } else {
                s = s.replace("%cost%", formattedPrice);
                s = s.replace("%next-level%", String.valueOf((int)nextLevel));
                if (this.getName().equalsIgnoreCase("capacity")) {
                    s = s.replace("%next-size%", String.valueOf((int)this.getNextSize(nextLevel)));
                }
            }
            toReturn.add(s);
        }
        return new ItemBuilder(Material.valueOf(this.plugin.getBankYML().getString("menus.bank-upgrade.capacity.material"))).setName(this.plugin.getBankYML().getString("menus.bank-upgrade.capacity.name")).setLore((ArrayList<String>)toReturn).setCustomModelData(this.plugin.getBankYML().getInt("menus.bank-upgrade.capacity.custom-model-data")).build();
    }

    @Override
    public void execute(Player player) {
        VSProfile profile = (VSProfile)VSProfile.getProfiles().get((Object)player.getUniqueId());
        Bank bank = profile.getBank();
        int nextLevel = this.getLevel() + 1;
        this.setLevel(nextLevel);
        bank.getBankUpgrades().remove(this);
        bank.getBankUpgrades().add(this);
        profile.save();
        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-upgrade").replace("%prefix%", this.plugin.getPrefix()).replace("%upgrade%", this.getName().toLowerCase()).replace("%level%", String.valueOf((int)this.getLevel()))));
    }

    private int getCurrentSize() {
        return this.plugin.getBankYML().getInt("upgrades.capacity.levels." + this.getLevel() + ".upgraded-capacity");
    }

    private int getNextSize(int level) {
        return this.plugin.getBankYML().getInt("upgrades.capacity.levels." + level + ".upgraded-capacity");
    }
}
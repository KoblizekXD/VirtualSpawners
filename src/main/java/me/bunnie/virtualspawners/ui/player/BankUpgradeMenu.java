/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.ui.player;

import java.util.HashMap;
import java.util.Map;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.upgrades.Upgrade;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BankUpgradeMenu
extends Menu {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final Bank spawnerBank;

    public BankUpgradeMenu(Bank spawnerBank) {
        this.spawnerBank = spawnerBank;
    }

    @Override
    public String getTitle(Player player) {
        return this.plugin.getBankYML().getString("menus.bank-upgrade.title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int i;
        HashMap buttons = new HashMap();
        for (i = 0; i < this.plugin.getUpgradeManager().getUpgradesFromType(Upgrade.Type.BANK).size(); ++i) {
            Upgrade upgrade = (Upgrade)this.plugin.getUpgradeManager().getUpgradesFromType(Upgrade.Type.BANK).get(i);
            buttons.put((Object)upgrade.getMenuSlot(), (Object)this.getUpgradeButton(upgrade));
        }
        if (this.plugin.getBankYML().getBoolean("menus.filler-item.enabled")) {
            for (i = 0; i < this.getSize(player); ++i) {
                if (buttons.containsKey((Object)i)) continue;
                buttons.put((Object)i, (Object)this.getFillerButton());
            }
        }
        return buttons;
    }

    private Button getUpgradeButton(final Upgrade upgrade) {
        final Economy economy = this.plugin.getEconomyHook().getEconomy();
        final int targetLevel = upgrade.getLevel() + 1;
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return upgrade.getIcon();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                if (upgrade.getLevel() == upgrade.getMaxLevel()) {
                    player.closeInventory();
                    player.sendMessage(ChatUtils.format(BankUpgradeMenu.this.plugin.getConfigYML().getString("messages.on-upgrade-fail-max").replace((CharSequence)"%prefix%", (CharSequence)BankUpgradeMenu.this.plugin.getPrefix()).replace((CharSequence)"%upgrade%", (CharSequence)upgrade.getName().toLowerCase())));
                } else if (economy.has((OfflinePlayer)player, (double)upgrade.getPrice(targetLevel))) {
                    economy.withdrawPlayer((OfflinePlayer)player, (double)upgrade.getPrice(targetLevel));
                    upgrade.execute(player);
                    this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                } else {
                    player.closeInventory();
                    player.sendMessage(ChatUtils.format(BankUpgradeMenu.this.plugin.getConfigYML().getString("messages.on-upgrade-fail-money").replace((CharSequence)"%prefix%", (CharSequence)BankUpgradeMenu.this.plugin.getPrefix())));
                }
            }
        };
    }

    private Button getFillerButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf((String)BankUpgradeMenu.this.plugin.getBankYML().getString("menus.filler-item.material"))).setName(BankUpgradeMenu.this.plugin.getBankYML().getString("menus.filler-item.name")).build();
            }
        };
    }

    @Override
    public int getSize(Player player) {
        return 27;
    }
}
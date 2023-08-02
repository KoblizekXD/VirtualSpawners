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
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.ui.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.bank.Bank;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.ui.player.BankUpgradeMenu;
import me.bunnie.virtualspawners.ui.player.SpawnerMangeMenu;
import me.bunnie.virtualspawners.ui.player.SpawnerUpgradeMenu;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SpawnerBankMenu
extends Menu {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final Bank spawnerBank;

    public SpawnerBankMenu(Bank spawnerBank) {
        this.spawnerBank = spawnerBank;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int i;
        Map<Integer, Button> buttons = new HashMap<>();
        for (i = 0; i < this.spawnerBank.getSpawners().size(); ++i) {
            buttons.put(i, this.getSpawnerButton(this.spawnerBank.getSpawners().get(i)));
        }
        if (this.plugin.getBankYML().getBoolean("menus.filler-item.enabled")) {
            for (i = 0; i < this.getSize(player); ++i) {
                if (buttons.containsKey(i)) continue;
                buttons.put(i, this.getFillerButton());
            }
        }
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-bank.spawner-upgrade.slot"), this.getSpawnerUpgradeButton());
        buttons.put(49, this.getFuelButton());
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-bank.bank-upgrade.slot"), this.getBankUpgradeButton());
        return buttons;
    }

    private Button getSpawnerUpgradeButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.spawner-bank.spawner-upgrade.material"))).setName(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.spawner-bank.spawner-upgrade.name")).setLore((ArrayList<String>) SpawnerBankMenu.this.plugin.getBankYML().getStringList("menus.spawner-bank.spawner-upgrade.lore")).setCustomModelData(SpawnerBankMenu.this.plugin.getBankYML().getInt("menus.spawner-bank.spawner-upgrade.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new SpawnerUpgradeMenu().getInventory(player);
            }
        };
    }

    @Override
    public String getTitle(Player player) {
        return "Bank";
    }

    private Button getSpawnerButton(final Spawner spawner) {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                String name = SpawnerBankMenu.this.plugin.getBankYML().getString("menus.spawner-bank.spawner.name").replace("%entity%", WordUtils.capitalize(spawner.getName().toLowerCase())).replace("%tier%", String.valueOf(spawner.getTier()));
                ArrayList toReturn = new ArrayList();
                for (String lore : SpawnerBankMenu.this.plugin.getBankYML().getStringList("menus.spawner-bank.spawner.lore")) {
                    lore = lore.replace("%alive-mobs%", String.valueOf(spawner.getAliveMobs()));
                    lore = lore.replace("%spawned%", String.valueOf(spawner.getSpawned()));
                    lore = lore.replace("%amount%", String.valueOf(spawner.getSize()));
                    lore = lore.replace("%tier%", String.valueOf(spawner.getTier()));
                    lore = lore.replace("%xp%", String.valueOf(spawner.getCollectedXP()));
                    lore = spawner.getDrops() == null || spawner.getDrops().isEmpty() ? lore.replace("%value%", String.valueOf(0)) : lore.replace("%value%", String.valueOf(spawner.getValue()));
                    toReturn.add(lore);
                }
                ItemBuilder builder = new ItemBuilder(Material.valueOf(SpawnerBankMenu.this.plugin.getConfigYML().getString("settings.redeemable-spawner.material")));
                if (builder.getMaterial().equals(Material.PLAYER_HEAD)) {
                    builder.setHead(SpawnerBankMenu.this.plugin.getConfigYML().getString("settings.redeemable-spawner.skull-texture"));
                }
                return builder.setName(ChatUtils.format(name)).setLore((ArrayList<String>) ChatUtils.format(toReturn)).setLore((ArrayList<String>)toReturn).setCustomModelData(SpawnerBankMenu.this.plugin.getConfig().getInt("menus.spawner-bank.spawner.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new SpawnerMangeMenu(SpawnerBankMenu.this.spawnerBank, spawner).getInventory(player);
            }
        };
    }

    private Button getBankUpgradeButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.spawner-bank.bank-upgrade.material"))).setName(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.spawner-bank.bank-upgrade.name")).setLore((ArrayList<String>) SpawnerBankMenu.this.plugin.getBankYML().getStringList("menus.spawner-bank.bank-upgrade.lore")).setCustomModelData(SpawnerBankMenu.this.plugin.getBankYML().getInt("menus.spawner-bank.bank-upgrade.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new BankUpgradeMenu(SpawnerBankMenu.this.spawnerBank).getInventory(player);
            }
        };
    }

    private Button getFillerButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.filler-item.material"))).setName(SpawnerBankMenu.this.plugin.getBankYML().getString("menus.filler-item.name")).build();
            }
        };
    }
    private Button getFuelButton() {
        SpawnerBankMenu spawnerBankMenu = this;
        return new Button() {
            @Override
            public ItemStack getItem(Player var1) {
                return new ItemBuilder(Material.FURNACE).setName("Fuels").build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new FuelSystemMenu(spawnerBankMenu).getInventory(player);
            }
        };
    }

    @Override
    public int getSize(Player player) {
        return 54;
    }
}
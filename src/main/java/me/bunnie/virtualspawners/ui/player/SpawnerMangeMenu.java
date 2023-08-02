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
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.ui.player.SpawnerDropMenu;
import me.bunnie.virtualspawners.ui.prompt.WithdrawConfirmationPrompt;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ExperienceUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SpawnerMangeMenu
extends Menu {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final Bank spawnerBank;
    private final Spawner spawner;
    private final VSProfile profile;

    public SpawnerMangeMenu(Bank spawnerBank, Spawner spawner) {
        this.spawnerBank = spawnerBank;
        this.profile = spawnerBank.getProfile();
        this.spawner = spawner;
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.format(this.plugin.getBankYML().getString("menus.spawner-manage.title").replace("%entity%", this.spawner.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-manage.withdraw-spawner.slot"), this.getSpawnerWithdrawButton());
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-manage.withdraw-item.slot"), this.getItemWithdrawButton());
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-manage.xp.slot"), this.getXPButton());
        // buttons.put(this.plugin.getBankYML().getInt("menus.spawner-manage.sell.slot"), this.getSellButton());
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-manage.drop.slot"), this.getUpgradeButton());
        if (this.plugin.getBankYML().getBoolean("menus.filler-item.enabled")) {
            for (int i = 0; i < this.getSize(player); ++i) {
                if (buttons.containsKey(i)) continue;
                buttons.put(i, this.getFillerButton());
            }
        }
        return buttons;
    }

    private Button getSpawnerWithdrawButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List lore = SpawnerMangeMenu.this.plugin.getBankYML().getStringList("menus.spawner-manage.withdraw-spawner.lore");
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.withdraw-spawner.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.withdraw-spawner.name")).setLore((ArrayList<String>) lore).setGlow(true).setCustomModelData(SpawnerMangeMenu.this.plugin.getConfig().getInt("menus.spawner-manage.withdraw-spawner.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new WithdrawConfirmationPrompt(SpawnerMangeMenu.this.profile, SpawnerMangeMenu.this.spawner, false).getInventory(player);
            }
        };
    }

    private Button getXPButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List lore = SpawnerMangeMenu.this.plugin.getBankYML().getStringList("menus.spawner-manage.xp.lore");
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.xp.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.xp.name")).setLore((ArrayList<String>) lore).setGlow(true).setCustomModelData(SpawnerMangeMenu.this.plugin.getConfig().getInt("menus.spawner-manage.xp.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                if (SpawnerMangeMenu.this.spawner.getCollectedXP() == 0) {
                    player.sendMessage(ChatUtils.format(SpawnerMangeMenu.this.plugin.getPrefix() + " #fcdfffYou dont have any XP to collect!"));
                    return;
                }
                player.sendMessage(ChatUtils.format(SpawnerMangeMenu.this.plugin.getBankYML().getString("messages.on-xp-collect").replace("%prefix%", SpawnerMangeMenu.this.plugin.getPrefix()).replace("%xp%", String.valueOf(SpawnerMangeMenu.this.spawner.getCollectedXP()))));
                ExperienceUtils.changeExp(player, SpawnerMangeMenu.this.spawner.getCollectedXP());
                SpawnerMangeMenu.this.spawner.setCollectedXP(0);
            }
        };
    }

    private Button getSellButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List lore = SpawnerMangeMenu.this.plugin.getBankYML().getStringList("menus.spawner-manage.sell.lore");
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.sell.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.sell.name")).setLore((ArrayList<String>) lore).setGlow(true).setCustomModelData(SpawnerMangeMenu.this.plugin.getConfig().getInt("menus.spawner-manage.sell.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                if (SpawnerMangeMenu.this.spawner.getDrops().isEmpty() || SpawnerMangeMenu.this.spawner.getDrops() == null) {
                    player.sendMessage(ChatUtils.format(SpawnerMangeMenu.this.plugin.getPrefix() + " #fcdfffYou dont have anything to sell!"));
                    return;
                }
                SpawnerMangeMenu.this.plugin.getEconomyHook().sell(SpawnerMangeMenu.this.profile, SpawnerMangeMenu.this.spawner);
            }
        };
    }

    private Button getItemWithdrawButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List lore = SpawnerMangeMenu.this.plugin.getBankYML().getStringList("menus.spawner-manage.withdraw-item.lore");
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.withdraw-item.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.withdraw-item.name")).setLore((ArrayList<String>) lore).setGlow(true).setCustomModelData(SpawnerMangeMenu.this.plugin.getConfig().getInt("menus.spawner-manage.withdraw-item.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                if (SpawnerMangeMenu.this.spawner.getDrops().isEmpty() || SpawnerMangeMenu.this.spawner.getDrops() == null) {
                    player.sendMessage(ChatUtils.format(SpawnerMangeMenu.this.plugin.getPrefix() + " #fcdfffYou dont have anything to withdraw!"));
                    return;
                }
                new WithdrawConfirmationPrompt(SpawnerMangeMenu.this.profile, SpawnerMangeMenu.this.spawner, true).getInventory(player);
            }
        };
    }

    private Button getUpgradeButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List lore = SpawnerMangeMenu.this.plugin.getBankYML().getStringList("menus.spawner-manage.drop.lore");
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.drop.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.spawner-manage.drop.name")).setLore((ArrayList<String>) lore).setGlow(true).setCustomModelData(SpawnerMangeMenu.this.plugin.getConfig().getInt("menus.spawner-manage.drop.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new SpawnerDropMenu(SpawnerMangeMenu.this.profile, SpawnerMangeMenu.this.spawner).getInventory(player);
            }
        };
    }

    private Button getFillerButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.filler-item.material"))).setName(SpawnerMangeMenu.this.plugin.getBankYML().getString("menus.filler-item.name")).build();
            }
        };
    }

    @Override
    public int getSize(Player player) {
        return 27;
    }
}
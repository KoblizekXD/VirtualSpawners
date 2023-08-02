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
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
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
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
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

public class SpawnerDropMenu
extends Menu {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final VSProfile profile;
    private final Spawner spawner;

    public SpawnerDropMenu(VSProfile profile, Spawner spawner) {
        this.profile = profile;
        this.spawner = spawner;
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.format(this.plugin.getBankYML().getString("menus.spawner-drops.title").replace("%entity%", this.spawner.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap buttons = new HashMap();
        buttons.put(this.plugin.getBankYML().getInt("menus.spawner-drops.drops.slot"), this.getDropsButton());
        if (this.plugin.getBankYML().getBoolean("menus.filler-item.enabled")) {
            for (int i = 0; i < this.getSize(player); ++i) {
                if (buttons.containsKey(i)) continue;
                buttons.put(i, this.getFillerButton());
            }
        }
        return buttons;
    }

    private Button getDropsButton() {
        Economy economy = this.plugin.getEconomyHook().getEconomy();
        final String currentDrop = this.plugin.getTiersYML().getString(this.spawner.getName() + ".tier." + this.spawner.getTier() + ".item-drop.name");
        final int nextTier = this.spawner.getTier() + 1;
        final int price = this.plugin.getTiersYML().getInt(this.spawner.getName() + ".tier." + nextTier + ".upgrade-price");
        final String nextDrop = this.plugin.getTiersYML().getString(this.spawner.getName() + ".tier." + nextTier + ".item-drop.name");
        final String formattedPrice = economy.format(price);
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                List<String> lore = SpawnerDropMenu.this.plugin.getBankYML().getStringList("menus.spawner-drops.drops.lore");
                ArrayList<String> toReturn = new ArrayList<>();
                for (String s : lore) {
                    s = s.replace("%current-drop%", currentDrop);
                    if (SpawnerDropMenu.this.tierIsValid(SpawnerDropMenu.this.spawner.getName(), nextTier)) {
                        s = s.replace("%next-drop%", nextDrop);
                    } else {
                        s = s.replace("%cost%", "$0");
                        s = s.replace("%next-drop%", SpawnerDropMenu.this.plugin.getBankYML().getString("menus.spawner-drops.replace.max-level").replace("%current-level%", String.valueOf(SpawnerDropMenu.this.spawner.getTier())).replace("%max-level%", String.valueOf(SpawnerDropMenu.this.spawner.getMaxTier())));
                    }
                    s = s.replace("%cost%", formattedPrice);
                    toReturn.add(s);
                }
                return new ItemBuilder(Material.valueOf(SpawnerDropMenu.this.plugin.getTiersYML().getString(SpawnerDropMenu.this.spawner.getName() + ".tier." + SpawnerDropMenu.this.spawner.getTier() + ".item-drop.material"))).setName(SpawnerDropMenu.this.plugin.getBankYML().getString("menus.spawner-drops.drops.name")).setLore(toReturn).setGlow(SpawnerDropMenu.this.plugin.getTiersYML().getBoolean(SpawnerDropMenu.this.spawner.getName() + ".tier." + SpawnerDropMenu.this.spawner.getTier() + ".item-drop.enchanted")).setCustomModelData(SpawnerDropMenu.this.plugin.getTiersYML().getInt(SpawnerDropMenu.this.spawner.getName() + ".tier." + nextTier + ".item-drop.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                this.update(player, getMenuMap().get(player.getUniqueId()));
                Economy economy = SpawnerDropMenu.this.plugin.getEconomyHook().getEconomy();
                if (SpawnerDropMenu.this.tierIsValid(SpawnerDropMenu.this.spawner.getName(), nextTier)) {
                    if (economy.has(player, price)) {
                        economy.withdrawPlayer(player, price);
                        SpawnerDropMenu.this.spawner.setTier(nextTier);
                        SpawnerDropMenu.this.profile.save();
                        this.update(player, getMenuMap().get(player.getUniqueId()));
                        player.sendMessage(ChatUtils.format(SpawnerDropMenu.this.plugin.getConfigYML().getString("messages.on-upgrade").replace("%prefix%", SpawnerDropMenu.this.plugin.getPrefix()).replace("%upgrade%", "drops").replace("%level%", currentDrop)));
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatUtils.format(SpawnerDropMenu.this.plugin.getConfigYML().getString("messages.on-upgrade-fail-money").replace("%prefix%", SpawnerDropMenu.this.plugin.getPrefix())));
                    }
                } else {
                    player.closeInventory();
                    player.sendMessage(ChatUtils.format(SpawnerDropMenu.this.plugin.getConfigYML().getString("messages.on-upgrade-fail-max").replace("%prefix%", SpawnerDropMenu.this.plugin.getPrefix()).replace("%upgrade%", "efficiency")));
                }
            }
        };
    }

    private Button getFillerButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf(SpawnerDropMenu.this.plugin.getBankYML().getString("menus.filler-item.material"))).setName(SpawnerDropMenu.this.plugin.getBankYML().getString("menus.filler-item.name")).build();
            }
        };
    }

    private boolean tierIsValid(String name, int tier) {
        for (String s : this.plugin.getTiersYML().getConfigurationSection(name + ".tier").getKeys(false)) {
            int i = Integer.parseInt(s);
            if (i != tier) continue;
            return true;
        }
        return false;
    }

    @Override
    public int getSize(Player player) {
        return 9;
    }
}
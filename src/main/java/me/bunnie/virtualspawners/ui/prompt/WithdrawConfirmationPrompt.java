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
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.ui.prompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.events.SpawnerWithdrawEvent;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class WithdrawConfirmationPrompt
extends Menu {
    private final VirtualSpawners plugin = VirtualSpawners.getInstance();
    private final VSProfile profile;
    private final Spawner spawner;
    private int withdrawingAmount = 1;
    private final boolean isItem;

    public WithdrawConfirmationPrompt(VSProfile profile, Spawner spawner, boolean isItem) {
        this.profile = profile;
        this.spawner = spawner;
        this.isItem = isItem;
    }

    @Override
    public String getTitle(Player player) {
        return this.plugin.getBankYML().getString("menus.withdraw-confirmation.title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int i;
        HashMap buttons = new HashMap();
        for (i = 0; i < 21; ++i) {
            if (i >= 3 && (i < 9 || i >= 12) && (i < 18 || i >= 21)) continue;
            buttons.put((Object)i, (Object)this.getConfirmButton());
        }
        buttons.put((Object)13, (Object)this.getSpawnerButton());
        for (i = 0; i < 27; ++i) {
            if (!(i >= 6 && i < 9 || i >= 15 && i < 18) && (i < 24 || i >= 27)) continue;
            buttons.put((Object)i, (Object)this.getCancelButton());
        }
        return buttons;
    }

    private Button getConfirmButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf((String)WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.confirm.material"))).setName(WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.confirm.name")).setLore((ArrayList<String>)((ArrayList)WithdrawConfirmationPrompt.this.plugin.getBankYML().getStringList("menus.withdraw-confirmation.confirm.lore"))).setCustomModelData(WithdrawConfirmationPrompt.this.plugin.getBankYML().getInt("menus.withdraw-confirmation.confirm.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                if (WithdrawConfirmationPrompt.this.isItem) {
                    int newAmount = (Integer)WithdrawConfirmationPrompt.this.spawner.getDrops().get((Object)WithdrawConfirmationPrompt.this.spawner.getMobDrop()) - WithdrawConfirmationPrompt.this.withdrawingAmount;
                    WithdrawConfirmationPrompt.this.spawner.getDrops().remove((Object)WithdrawConfirmationPrompt.this.spawner.getMobDrop());
                    WithdrawConfirmationPrompt.this.spawner.getDrops().put(WithdrawConfirmationPrompt.this.spawner.getMobDrop(), newAmount);
                    ItemStack itemStack = WithdrawConfirmationPrompt.this.spawner.getMobDrop();
                    itemStack.setAmount(WithdrawConfirmationPrompt.this.withdrawingAmount);
                    player.getInventory().addItem(new ItemStack[]{itemStack});
                    player.sendMessage(ChatUtils.format(WithdrawConfirmationPrompt.this.plugin.getConfigYML().getString("messages.on-withdraw-item").replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.withdrawingAmount)).replace((CharSequence)"%prefix%", (CharSequence)WithdrawConfirmationPrompt.this.plugin.getPrefix())));
                    return;
                }
                WithdrawConfirmationPrompt.this.plugin.getServer().getPluginManager().callEvent((Event)new SpawnerWithdrawEvent(player, WithdrawConfirmationPrompt.this.profile, WithdrawConfirmationPrompt.this.spawner, WithdrawConfirmationPrompt.this.withdrawingAmount));
            }
        };
    }

    private Button getCancelButton() {
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.valueOf((String)WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.cancel.material"))).setName(WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.cancel.name")).setLore((ArrayList<String>)((ArrayList)WithdrawConfirmationPrompt.this.plugin.getBankYML().getStringList("menus.withdraw-confirmation.cancel.lore"))).setCustomModelData(WithdrawConfirmationPrompt.this.plugin.getBankYML().getInt("menus.withdraw-confirmation.cancel.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
            }
        };
    }

    private Button getSpawnerButton() {
        if (this.isItem) {
            return new Button(){

                @Override
                public ItemStack getItem(Player player) {
                    String name = WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.item.name").replace((CharSequence)"%item%", (CharSequence)WithdrawConfirmationPrompt.this.spawner.getMobDropName());
                    ArrayList toReturn = new ArrayList();
                    for (String lore : WithdrawConfirmationPrompt.this.plugin.getBankYML().getStringList("menus.withdraw-confirmation.item.lore")) {
                        lore = lore.replace((CharSequence)"%withdraw-amount%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.withdrawingAmount));
                        lore = WithdrawConfirmationPrompt.this.spawner.getDrops() == null || WithdrawConfirmationPrompt.this.spawner.getDrops().isEmpty() ? lore.replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)0)) : lore.replace((CharSequence)"%amount%", (CharSequence)String.valueOf((Object)WithdrawConfirmationPrompt.this.spawner.getDrops().get((Object)WithdrawConfirmationPrompt.this.spawner.getMobDrop())));
                        toReturn.add((Object)lore);
                    }
                    return new ItemBuilder(WithdrawConfirmationPrompt.this.spawner.getMobDrop().getType()).setName(name).setGlow(WithdrawConfirmationPrompt.this.spawner.getMobDrop().getItemMeta().hasEnchants()).setLore((ArrayList<String>)toReturn).setCustomModelData(WithdrawConfirmationPrompt.this.plugin.getTiersYML().getInt(WithdrawConfirmationPrompt.this.spawner.getName() + ".tier." + WithdrawConfirmationPrompt.this.spawner.getTier() + ".item-drop.custom-model-data")).build();
                }

                @Override
                public void onButtonClick(Player player, int slot, ClickType clickType) {
                    int items = (Integer)WithdrawConfirmationPrompt.this.spawner.getDrops().get((Object)WithdrawConfirmationPrompt.this.spawner.getMobDrop());
                    if (clickType.isLeftClick()) {
                        if (WithdrawConfirmationPrompt.this.withdrawingAmount == items) {
                            this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                            return;
                        }
                        if (items > WithdrawConfirmationPrompt.this.withdrawingAmount) {
                            ++WithdrawConfirmationPrompt.this.withdrawingAmount;
                            this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                            return;
                        }
                    }
                    if (clickType.isRightClick() && WithdrawConfirmationPrompt.this.withdrawingAmount > 1) {
                        --WithdrawConfirmationPrompt.this.withdrawingAmount;
                        this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                    }
                }
            };
        }
        return new Button(){

            @Override
            public ItemStack getItem(Player player) {
                String name = WithdrawConfirmationPrompt.this.plugin.getBankYML().getString("menus.withdraw-confirmation.spawner.name").replace((CharSequence)"%entity%", (CharSequence)WordUtils.capitalize((String)WithdrawConfirmationPrompt.this.spawner.getName().toLowerCase())).replace((CharSequence)"%tier%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.spawner.getTier()));
                ArrayList toReturn = new ArrayList();
                for (String lore : WithdrawConfirmationPrompt.this.plugin.getBankYML().getStringList("menus.withdraw-confirmation.spawner.lore")) {
                    lore = lore.replace((CharSequence)"%amount%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.spawner.getSize()));
                    lore = lore.replace((CharSequence)"%tier%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.spawner.getTier()));
                    lore = lore.replace((CharSequence)"%withdraw-amount%", (CharSequence)String.valueOf((int)WithdrawConfirmationPrompt.this.withdrawingAmount));
                    lore = WithdrawConfirmationPrompt.this.spawner.getDrops() == null || WithdrawConfirmationPrompt.this.spawner.getDrops().isEmpty() ? lore.replace((CharSequence)"%value%", (CharSequence)String.valueOf((int)0)) : lore.replace((CharSequence)"%value%", (CharSequence)String.valueOf((double)WithdrawConfirmationPrompt.this.spawner.getValue()));
                    toReturn.add((Object)lore);
                }
                ItemBuilder builder = new ItemBuilder(Material.valueOf((String)WithdrawConfirmationPrompt.this.plugin.getConfigYML().getString("settings.redeemable-spawner.material")));
                if (builder.getMaterial().equals((Object)Material.PLAYER_HEAD)) {
                    builder.setHead(WithdrawConfirmationPrompt.this.plugin.getConfigYML().getString("settings.redeemable-spawner.skull-texture"));
                }
                return builder.setName(ChatUtils.format(name)).setLore((ArrayList<String>)((ArrayList)ChatUtils.format((List<String>)toReturn))).setCustomModelData(WithdrawConfirmationPrompt.this.plugin.getConfig().getInt("menus.withdraw-confirmation.spawner.custom-model-data")).build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                int spawnerSize = WithdrawConfirmationPrompt.this.spawner.getSize();
                if (clickType.isLeftClick()) {
                    if (WithdrawConfirmationPrompt.this.withdrawingAmount == spawnerSize) {
                        this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                        return;
                    }
                    if (spawnerSize > WithdrawConfirmationPrompt.this.withdrawingAmount) {
                        ++WithdrawConfirmationPrompt.this.withdrawingAmount;
                        this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                        return;
                    }
                }
                if (clickType.isRightClick() && WithdrawConfirmationPrompt.this.withdrawingAmount > 1) {
                    --WithdrawConfirmationPrompt.this.withdrawingAmount;
                    this.update(player, (Menu) getMenuMap().get((Object)player.getUniqueId()));
                }
            }
        };
    }
}
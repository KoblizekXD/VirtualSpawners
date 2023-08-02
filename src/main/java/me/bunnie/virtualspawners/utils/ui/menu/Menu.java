/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package me.bunnie.virtualspawners.utils.ui.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu {
    private static final Map<UUID, Menu> menuMap = new HashMap();

    public abstract String getTitle(Player var1);

    public abstract Map<Integer, Button> getButtons(Player var1);

    public int getSize(Player player) {
        return -1;
    }

    public Inventory getInventory(Player player) {
        Map<Integer, Button> buttonMap = this.getButtons(player);
        int size = this.getSize(player) == -1 ? this.calculateSize(buttonMap) : this.getSize(player);
        String title = this.getTitle(player);
        if (title == null) {
            title = "Failed to load title";
        }
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        Inventory toReturn = Bukkit.createInventory(player, size, title);
        Menu previous = menuMap.get(player.getUniqueId());
        if (player.getOpenInventory().getTopInventory() != null) {
            if (previous == null) {
                player.closeInventory();
            }
        } else {
            int previousSize = player.getOpenInventory().getTopInventory().getSize();
            String previousTitle = player.getOpenInventory().getTitle();
            if (previousSize == size && previousTitle.equals(title)) {
                toReturn = player.getOpenInventory().getTopInventory();
            } else {
                player.closeInventory();
            }
        }
        for (Map.Entry buttonEntry : buttonMap.entrySet()) {
            try {
                toReturn.setItem(((Integer)buttonEntry.getKey()).intValue(), ((Button)buttonEntry.getValue()).getItem(player));
            } catch (Exception e) {
                System.err.println("Can't add more!");
            }
        }
        this.onOpen(player, toReturn);
        return toReturn;
    }

    private void onOpen(Player player, Inventory inventory) {
        player.openInventory(inventory);
        menuMap.put(player.getUniqueId(), this);
    }

    public void onClose(Player player) {
        menuMap.remove(player.getUniqueId());
    }

    private int calculateSize(Map<Integer, Button> buttons) {
        int highest = 0;
        Iterator iterator = buttons.keySet().iterator();
        while (iterator.hasNext()) {
            int buttonValue = (Integer)iterator.next();
            if (buttonValue <= highest) continue;
            highest = buttonValue;
        }
        return (int)(Math.ceil((double)(highest + 1) / 9.0) * 9.0);
    }

    public static Map<UUID, Menu> getMenuMap() {
        return menuMap;
    }
}
/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.util.Map
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package me.bunnie.virtualspawners.listeners;

import java.util.Map;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.ui.player.FuelSystemMenu;
import me.bunnie.virtualspawners.utils.Smelter;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());
        if (menu == null) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getType() != InventoryType.CHEST && !(menu instanceof FuelSystemMenu)) {
            return;
        }
        if (!(menu instanceof FuelSystemMenu)) event.setCancelled(true);
        int slot = event.getSlot();
        Map<Integer, Button> buttons = menu.getButtons(player);
        if (buttons.containsKey(slot) && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            if (!event.isCancelled()) event.setCancelled(true);
            Button button = buttons.get(slot);
            button.onButtonClick(player, slot, event.getClick());
        } else if (menu instanceof FuelSystemMenu) {
            if (event.getClickedInventory().equals(menu.getInventory(player)) && event.getCursor() == null) {
                event.setCancelled(true);
            } else if (event.getClickedInventory().equals(player.getInventory())) {
                event.setCancelled(true);
                var smelter = Smelter.getOrCreateSmelter(player);
                smelter.addMaterial(event.getCurrentItem());
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());
        if (menu == null) {
            return;
        }
        menu.onClose(player);
        Menu.getMenuMap().remove(player.getUniqueId());
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());
        if (menu == null) {
            return;
        }
        menu.onClose(player);
        Menu.getMenuMap().remove(player.getUniqueId());
    }
}
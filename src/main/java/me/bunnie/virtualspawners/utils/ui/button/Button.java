/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package me.bunnie.virtualspawners.utils.ui.button;

import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
    public abstract ItemStack getItem(Player var1);

    public void onButtonClick(Player player, int slot, ClickType clickType) {
    }

    public void update(Player player, Menu menu) {
        player.getOpenInventory().getTopInventory().clear();
        player.getOpenInventory().getTopInventory().setContents(menu.getInventory(player).getContents());
    }
}
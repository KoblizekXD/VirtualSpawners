package me.bunnie.virtualspawners.ui.player;

import com.google.common.collect.Maps;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.utils.ItemBuilder;
import me.bunnie.virtualspawners.utils.Smelter;
import me.bunnie.virtualspawners.utils.ui.button.Button;
import me.bunnie.virtualspawners.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelSystemMenu extends Menu {
    public static final List<Material> SMELTABLES = List.of(
            Material.BLAZE_ROD
    );
    public static final Map<Player, Smelter> SMELTINGS = new HashMap<>();

    private final SpawnerBankMenu menu;

    public FuelSystemMenu(SpawnerBankMenu menu) {
        this.menu = menu;
    }

    @Override
    public String getTitle(Player var1) {
        return "Fuels";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, getBackButton());
        for (int i = 0; i < Smelter.getOrCreateSmelter(player).getQueue().size(); i++) {
            buttons.put(i+1, getItemButton(Smelter.getOrCreateSmelter(player).getQueue().get(i)));
        }
        return buttons;
    }

    private Button getItemButton(ItemStack stack) {
        return new Button() {
            @Override
            public ItemStack getItem(Player var1) {
                return stack;
            }
        };
    }

    @Override
    public int getSize(Player player) {
        return 9;
    }

    private Button getBackButton() {
        return new Button() {
            @Override
            public ItemStack getItem(Player var1) {
                return new ItemBuilder(Material.SPAWNER).setName("Back").build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                menu.getInventory(player);
            }
        };
    }
}

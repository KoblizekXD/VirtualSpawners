/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Objects
 *  java.util.function.Consumer
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.bunnie.virtualspawners.listeners;

import java.util.Objects;
import java.util.function.Consumer;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.events.SpawnerRedeemEvent;
import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.utils.ChatUtils;
import me.bunnie.virtualspawners.utils.UpdateUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ProfileListener
implements Listener {
    private final VirtualSpawners plugin;

    public ProfileListener(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        new VSProfile(event.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() || player.hasPermission("vs.commands.help")) {
            new UpdateUtils(this.plugin, 107908).getLatestVersion(version -> {
                if (this.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #fcdfffVirtualSpawners is up to date!"));
                } else {
                    player.sendMessage(ChatUtils.format(this.plugin.getPrefix() + " #ffcdd2Your plugin is out of date! download the latest version for bug fixes and newly added features! "));
                    player.sendMessage(ChatUtils.format("#c9eff9https://www.spigotmc.org/resources/virtrualspawners.107908/"));
                }
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        VSProfile profile = (VSProfile)VSProfile.getProfiles().get(player.getUniqueId());
        profile.save();
        VSProfile.getProfiles().remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : data.getKeys()) {
            if (!key.getKey().equalsIgnoreCase("entity")) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        NamespacedKey entityKey;
        PersistentDataContainer data;
        Player player = event.getPlayer();
        VSProfile profile = (VSProfile)VSProfile.getProfiles().get(player.getUniqueId());
        int capacity = profile.getBank().getCurrentSize();
        ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }
        if (itemStack.getType().equals(Material.valueOf((String)this.plugin.getConfigYML().getString("settings.redeemable-spawner.material"))) && (data = ((ItemMeta)Objects.requireNonNull(itemStack.getItemMeta())).getPersistentDataContainer()).has(entityKey = new NamespacedKey((Plugin)this.plugin, "entity"), PersistentDataType.STRING)) {
            if (profile.getBank().getTotalSpawners() == capacity) {
                player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-redeem-fail").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix())));
                return;
            }
            NamespacedKey tierKey = new NamespacedKey((Plugin)this.plugin, "tier");
            NamespacedKey sizeKey = new NamespacedKey((Plugin)this.plugin, "size");
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                String name = (String)data.get(entityKey, PersistentDataType.STRING);
                Integer tier = (Integer)data.get(tierKey, PersistentDataType.INTEGER);
                Integer size = (Integer)data.get(sizeKey, PersistentDataType.INTEGER);
                if (itemStack.getAmount() > 1) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                } else {
                    player.getInventory().remove(itemStack);
                }
                this.plugin.getServer().getPluginManager().callEvent((Event)new SpawnerRedeemEvent(player, name, tier, size));
            }
            event.setCancelled(true);
        }
    }
}
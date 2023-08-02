/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.bunnie.virtualspawners.events;

import me.bunnie.virtualspawners.spawner.Spawner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnerSpawnEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Spawner spawner;

    public SpawnerSpawnEvent(Player player, Spawner spawner) {
        this.player = player;
        this.spawner = spawner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Spawner getSpawner() {
        return this.spawner;
    }
}
/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.bunnie.virtualspawners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnerRedeemEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String name;
    private final Integer tier;
    private final Integer size;

    public SpawnerRedeemEvent(Player player, String name, Integer tier, Integer size) {
        this.player = player;
        this.name = name;
        this.tier = tier;
        this.size = size;
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

    public String getName() {
        return this.name;
    }

    public Integer getTier() {
        return this.tier;
    }

    public Integer getSize() {
        return this.size;
    }
}
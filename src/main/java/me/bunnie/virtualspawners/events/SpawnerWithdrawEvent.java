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

import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnerWithdrawEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final VSProfile profile;
    private final Spawner spawner;
    private final int withdrawingAmount;

    public SpawnerWithdrawEvent(Player player, VSProfile profile, Spawner spawner, int withdrawingAmount) {
        this.player = player;
        this.profile = profile;
        this.spawner = spawner;
        this.withdrawingAmount = withdrawingAmount;
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

    public VSProfile getProfile() {
        return this.profile;
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    public int getWithdrawingAmount() {
        return this.withdrawingAmount;
    }
}
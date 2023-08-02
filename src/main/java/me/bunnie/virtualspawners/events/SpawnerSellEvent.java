/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.bunnie.virtualspawners.events;

import me.bunnie.virtualspawners.profile.VSProfile;
import me.bunnie.virtualspawners.spawner.Spawner;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnerSellEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final VSProfile profile;
    private final Spawner spawner;

    public SpawnerSellEvent(VSProfile profile, Spawner spawner) {
        this.profile = profile;
        this.spawner = spawner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public VSProfile getProfile() {
        return this.profile;
    }

    public Spawner getSpawner() {
        return this.spawner;
    }
}
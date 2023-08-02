/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 */
package me.bunnie.virtualspawners.upgrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.bunnie.virtualspawners.upgrades.Upgrade;

public class UpgradeManager {
    private final Map<String, Upgrade> upgrades = new HashMap();

    public void register(Upgrade upgrade) {
        if (this.upgrades.containsKey(upgrade.getName())) {
            return;
        }
        this.upgrades.put(upgrade.getName(), upgrade);
    }

    public Upgrade getUpgrade(String name) {
        return (Upgrade)this.upgrades.get(name);
    }

    public List<Upgrade> getUpgradesFromType(Upgrade.Type upgradeType) {
        ArrayList toReturn = new ArrayList();
        for (Upgrade upgrade : this.upgrades.values()) {
            if (!upgrade.getType().equals(upgradeType)) continue;
            toReturn.add(upgrade);
        }
        return toReturn;
    }

    public List<Upgrade> getUpgrades() {
        ArrayList toReturn = new ArrayList();
        Iterator iterator = this.upgrades.values().iterator();
        if (iterator.hasNext()) {
            Upgrade upgrade = (Upgrade)iterator.next();
            toReturn.add(upgrade);
            return toReturn;
        }
        return toReturn;
    }
}
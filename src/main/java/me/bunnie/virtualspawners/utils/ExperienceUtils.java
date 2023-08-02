/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  org.bukkit.entity.Player
 */
package me.bunnie.virtualspawners.utils;

import org.bukkit.entity.Player;

public class ExperienceUtils {
    public static int getExp(Player player) {
        return ExperienceUtils.getExpFromLevel(player.getLevel()) + Math.round((float)((float)ExperienceUtils.getExpToNext(player.getLevel()) * player.getExp()));
    }

    public static int getExpFromLevel(int level) {
        if (level > 30) {
            return (int)(4.5 * (double)level * (double)level - 162.5 * (double)level + 2220.0);
        }
        if (level > 15) {
            return (int)(2.5 * (double)level * (double)level - 40.5 * (double)level + 360.0);
        }
        return level * level + 6 * level;
    }

    public static double getLevelFromExp(long exp) {
        int level = ExperienceUtils.getIntLevelFromExp(exp);
        float remainder = (float)exp - (float)ExperienceUtils.getExpFromLevel(level);
        float progress = remainder / (float)ExperienceUtils.getExpToNext(level);
        return (double)level + (double)progress;
    }

    public static int getIntLevelFromExp(long exp) {
        if (exp > 1395L) {
            return (int)((Math.sqrt((double)((double)(72L * exp) - 54215.0)) + 325.0) / 18.0);
        }
        if (exp > 315L) {
            return (int)(Math.sqrt((double)((double)(40L * exp) - 7839.0)) / 10.0 + 8.1);
        }
        if (exp > 0L) {
            return (int)(Math.sqrt((double)((double)exp + 9.0)) - 3.0);
        }
        return 0;
    }

    private static int getExpToNext(int level) {
        if (level >= 30) {
            return level * 9 - 158;
        }
        if (level >= 15) {
            return level * 5 - 38;
        }
        return level * 2 + 7;
    }

    public static void changeExp(Player player, int exp) {
        if ((exp += ExperienceUtils.getExp(player)) < 0) {
            exp = 0;
        }
        double levelAndExp = ExperienceUtils.getLevelFromExp(exp);
        int level = (int)levelAndExp;
        player.setLevel(level);
        player.setExp((float)(levelAndExp - (double)level));
    }
}
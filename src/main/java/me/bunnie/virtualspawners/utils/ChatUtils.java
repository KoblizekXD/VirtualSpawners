/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  net.md_5.bungee.api.ChatColor
 */
package me.bunnie.virtualspawners.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ChatUtils {
    public static String format(String message) {
        Pattern pattern = Pattern.compile((String)"#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher((CharSequence)message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace((CharSequence)color, (CharSequence)("" + ChatColor.of((String)color)));
            matcher = pattern.matcher((CharSequence)message);
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)message);
    }

    public static List<String> format(List<String> messages) {
        ArrayList<String> toReturn = new ArrayList<>();
        messages.forEach(message -> toReturn.add(ChatUtils.format(message)));
        return toReturn;
    }

    public static String removeColorCodes(String input) {
        return input.replaceAll("(?i)&[0-9A-FK-OR]", "").replaceAll("#[0-9A-Fa-f]{6}", "");
    }

    public static String fixCapitalisation(String message) {
        String[] arr = message.split("_");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Character.toUpperCase((char)arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
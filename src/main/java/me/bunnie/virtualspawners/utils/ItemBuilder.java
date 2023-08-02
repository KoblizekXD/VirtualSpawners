/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  java.lang.IllegalAccessException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Field
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 *  java.util.UUID
 *  org.bukkit.Color
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BannerMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.bunnie.virtualspawners.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ItemBuilder {
    private ItemStack stack;

    public ItemBuilder(Material mat) {
        this.stack = new ItemStack(mat);
    }

    public ItemBuilder(Material mat, short sh) {
        this.stack = new ItemStack(mat, 1, sh);
    }

    public ItemMeta getItemMeta() {
        return this.stack.getItemMeta();
    }

    public ItemBuilder setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta)this.stack.getItemMeta();
        meta.setColor(color);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDurability(int dur) {
        this.stack.setDurability((short)dur);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            this.addEnchant(Enchantment.KNOCKBACK, 1);
            this.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            ItemMeta meta = this.getItemMeta();
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                meta.removeEnchant(enchantment);
            }
        }
        return this;
    }

    public ItemBuilder setBannerColor(DyeColor color) {
        BannerMeta meta = (BannerMeta)this.stack.getItemMeta();
        meta.setBaseColor(color);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean b) {
        this.stack.getItemMeta().setUnbreakable(b);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setHead(String value) {
        SkullMeta meta = (SkullMeta)this.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gameProfile);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setName(String displayname) {
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatUtils.format(displayname));
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setItemStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = this.getItemMeta();
        meta.setLore(ChatUtils.format(lore));
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String[] lore) {
        ItemMeta meta = this.getItemMeta();
        meta.setLore(ChatUtils.format(Arrays.asList(lore)));
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addPDC(String key, PersistentDataType type, Object value) {
        ItemMeta meta = this.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(VirtualSpawners.getInstance(), key), type, value);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = this.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(int i) {
        ItemMeta meta = this.getItemMeta();
        if (i == 0) {
            this.setItemMeta(meta);
            return this;
        }
        meta.setCustomModelData(Integer.valueOf(i));
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(new ItemFlag[]{flag});
        this.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.stack;
    }

    public Material getMaterial() {
        return this.stack.getType();
    }
}
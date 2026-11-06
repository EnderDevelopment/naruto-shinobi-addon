package com.enderdevelopment.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin {
    private NamespacedKey jutsuKey;

    @Override
    public void onEnable() {
        jutsuKey = new NamespacedKey(this, "jutsu");
        getLogger().info("NarutoAddon enabled. Use /naruto to choose a jutsu.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NarutoAddon disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            sendMenu(player);
            return true;
        }
        String jutsu = args[0].toLowerCase(Locale.ROOT);
        switch (jutsu) {
            case "rasengan" -> giveJutsu(player, "Rasengan", Material.HEART_OF_THE_SEA, NamedTextColor.AQUA, "sharpness", 2);
            case "kunai" -> giveJutsu(player, "Kunai", Material.IRON_SWORD, NamedTextColor.GRAY, "sharpness", 3);
            case "shuriken" -> giveJutsu(player, "Shuriken", Material.TRIDENT, NamedTextColor.WHITE, "impaling", 2);
            case "chidori" -> giveJutsu(player, "Chidori", Material.LIGHTNING_ROD, NamedTextColor.BLUE, null, 0);
            default -> {
                player.sendMessage(Component.text("Tecnica desconhecida. Use /naruto para ver as tecnicas.", NamedTextColor.RED));
                return true;
            }
        }
        return true;
    }

    private void sendMenu(Player player) {
        player.sendMessage(Component.text("=== Naruto: Tecnicas Ninja ===", NamedTextColor.GOLD));
        player.sendMessage(Component.text("/naruto rasengan", NamedTextColor.AQUA).append(Component.text(" - esfera de chakra", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/naruto chidori", NamedTextColor.BLUE).append(Component.text(" - raio concentrado", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/naruto kunai", NamedTextColor.GRAY).append(Component.text(" - lamina ninja", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/naruto shuriken", NamedTextColor.WHITE).append(Component.text(" - arma ninja", NamedTextColor.GRAY)));
    }

    private void giveJutsu(Player player, String name, Material material, NamedTextColor color, String enchantmentKey, int level) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(Component.text("Nao foi possivel criar esta tecnica.", NamedTextColor.RED));
            return;
        }
        meta.displayName(Component.text(name, color));
        meta.lore(List.of(Component.text("Tecnica ninja de Naruto", NamedTextColor.DARK_GRAY),
                Component.text("Use sua criatividade para lutar!", NamedTextColor.GRAY)));
        meta.getPersistentDataContainer().set(jutsuKey, PersistentDataType.STRING, name.toLowerCase(Locale.ROOT));
        if (enchantmentKey != null) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey));
            if (enchantment != null) {
                meta.addEnchant(enchantment, level, true);
            }
        }
        item.setItemMeta(meta);
        player.getInventory().addItem(item).values().forEach(dropped -> player.getWorld().dropItemNaturally(player.getLocation(), dropped));
        player.sendMessage(Component.text("Voce recebeu: " + name + "! Dattebayo!", NamedTextColor.GOLD));
    }
}

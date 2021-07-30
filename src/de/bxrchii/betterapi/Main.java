package de.bxrchii.betterapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§8» §7Thanks for using BetterLabyAPI");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Status§7: §8[§aOnline§8]");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Creator§7: §bBxrchii");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Version§7: §c1.0");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8» §7Thanks for using BetterLabyAPI");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Status§7: §8[§cOffline§8]");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Creator§7: §bBxrchii");
        Bukkit.getConsoleSender().sendMessage("§8» §ePlugin-Version§7: §c1.0");
    }

}

package ru.Aleksa.OpRegions;

import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import ru.Aleksa.OpRegions.utils.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

public class OpRegions extends JavaPlugin
{
    static ArrayList<String> protectedRegions;
    static ArrayList<String> allowed;
    public static FileConfiguration cfg;
    
    static {
        OpRegions.protectedRegions = new ArrayList<String>();
        OpRegions.allowed = new ArrayList<String>();
    }
    
    public void onEnable() {
        OpRegions.cfg = this.getConfig();
        if (OpRegions.cfg.getString("protectedMsg") == null) {
            OpRegions.cfg.set("protectedMsg", (Object)"&cRegion is protected");
            OpRegions.cfg.createSection("protected");
            OpRegions.cfg.createSection("allowed");
            this.saveConfig();
        }
        this.reload();
        Bukkit.getPluginManager().registerEvents((Listener)new EventListener(), (Plugin)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("oprgs")) {
            if (!sender.hasPermission("oprgs.reload") || !(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "[OpRGs] You don't have enough permissions");
                return true;
            }
            this.reload();
            sender.sendMessage(ChatColor.GREEN + "[OpRGs] Config reloaded");
        }
        return true;
    }
    
    private void reload() {
        this.reloadConfig();
        OpRegions.cfg = this.getConfig();
        OpRegions.protectedRegions.clear();
        for (final String region : OpRegions.cfg.getStringList("protected")) {
            OpRegions.protectedRegions.add(region.toLowerCase());
        }
        for (final String allowed : OpRegions.cfg.getStringList("allowed")) {
            OpRegions.allowed.add(allowed);
        }
    }
    
    public static boolean isProtected(final String region) {
        return OpRegions.protectedRegions.contains(region.toLowerCase());
    }
    
    public static boolean isAllowed(final String player) {
        return OpRegions.allowed.contains(player.toLowerCase());
    }
}

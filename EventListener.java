package ru.Aleksa.OpRegions.utils;

import java.util.Iterator;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import ru.Aleksa.OpRegions.OpRegions;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.Bukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.event.Listener;

public class EventListener implements Listener
{
    private final WorldGuardPlugin wg;
    
    public EventListener() {
        this.wg = (WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard");
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        if (this.isProtected(p, e.getBlock())) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', OpRegions.cfg.getString("protectedMsg")));
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (this.isProtected(p, e.getBlock())) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', OpRegions.cfg.getString("protectedMsg")));
            e.setCancelled(true);
        }
    }
    
    private boolean isProtected(final Player p, final Block block) {
        if (OpRegions.isAllowed(p.getName())) {
            return false;
        }
        ProtectedRegion protectedRegion = null;
        for (final ProtectedRegion region : this.wg.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
            if (OpRegions.isProtected(region.getId())) {
                protectedRegion = region;
            }
        }
        for (final ProtectedRegion region : this.wg.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
            if (protectedRegion == null) {
                break;
            }
            if (region.getPriority() > protectedRegion.getPriority() && !OpRegions.isProtected(region.getId())) {
                return false;
            }
        }
        return protectedRegion != null;
    }
}

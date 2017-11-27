package duel.listeners;

import org.bukkit.entity.*;
import duel.*;
import duel.objects.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener
{
    public static List<Player> noMove;
    
    static {
        PlayerEvents.noMove = new ArrayList<Player>();
    }
    
    @EventHandler
    public void quit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        Duel.request.remove(p);
        for (final Player pl : Duel.request.keySet()) {
            Duel.request.get(pl).remove(p);
        }
        for (final Arena a : Duel.arenas) {
            if (a.isRunning() && (a.getPlayer().equals(p) || a.getPlayer2().equals(p))) {
                final Player k = a.getPlayer().equals(p) ? a.getPlayer2() : a.getPlayer();
                if (a.getPlayer().equals(p)) {
                    p.getInventory().setArmorContents(a.getArmor());
                    p.getInventory().setContents(a.getInv());
                    p.teleport(a.getLoc());
                    a.setPlayer(null);
                    a.setInv(null);
                    a.setArmor(null);
                    a.setLoc(null);
                }
                else {
                    p.getInventory().setArmorContents(a.getArmor2());
                    p.getInventory().setContents(a.getInv2());
                    p.teleport(a.getLoc2());
                    a.setPlayer2(null);
                    a.setInv2(null);
                    a.setArmor2(null);
                    a.setLoc2(null);
                }
                a.finish(k);
                Duel.plugin.getServer().broadcastMessage(String.valueOf(Duel.s) + "§e" + k.getName() + " §7beat §e" + p.getName() + " §7in the duel arena!");
                break;
            }
        }
    }
    
    @EventHandler
    public void move(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (PlayerEvents.noMove.contains(p) && (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ())) {
            p.teleport(e.getFrom());
        }
    }
    
    @EventHandler
    public void dmg(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && PlayerEvents.noMove.contains(e.getEntity())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void death(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (!(p.getKiller() instanceof Player)) {
            return;
        }
        final Player k = p.getKiller();
        if (p.hasMetadata("no_drop_duel")) {
            p.removeMetadata("no_drop_duel", (Plugin)Duel.plugin);
            k.removeMetadata("no_drop_duel", (Plugin)Duel.plugin);
            e.getDrops().clear();
        }
        for (final Arena a : Duel.arenas) {
            if (a.isRunning() && (a.getPlayer().equals(p) || a.getPlayer2().equals(p))) {
                a.finish(k);
                Duel.plugin.getServer().broadcastMessage(String.valueOf(Duel.s) + "§e" + k.getName() + " §7beat §e" + p.getName() + " §7in the duel arena!");
                break;
            }
        }
    }
    
    @EventHandler
    public void cmd(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (p.hasMetadata("no_drop_duel") && !e.getMessage().equalsIgnoreCase("/duel leave") && !e.getMessage().equalsIgnoreCase("/duel quit")) {
            e.setCancelled(true);
            p.sendMessage(String.valueOf(Duel.s) + "§eYou cannot use commands in a duel!");
        }
    }
}

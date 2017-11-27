package duel.objects;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import duel.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import duel.listeners.*;
import org.bukkit.scheduler.*;
import java.util.*;

public class Arena
{
    private String name;
    private Location spawn;
    private Location spawn2;
    private Player player;
    private Player player2;
    private ItemStack[] inv;
    private ItemStack[] inv2;
    private ItemStack[] armor;
    private ItemStack[] armor2;
    private Location loc;
    private Location loc2;
    private List<ItemStack> wager;
    private boolean running;
    
    public Arena(final String name, final Location spawn, final Location spawn2) {
        this.name = name;
        this.spawn = spawn;
        this.spawn2 = spawn2;
        this.wager = new ArrayList<ItemStack>();
    }
    
    public Arena(final String name) {
        this.name = name;
        this.wager = new ArrayList<ItemStack>();
    }
    
    public void start(final Player p, final Player pl, final Inventory inv) {
        this.running = true;
        this.player = p;
        this.player2 = pl;
        this.armor = p.getInventory().getArmorContents();
        this.armor2 = pl.getInventory().getArmorContents();
        this.inv = p.getInventory().getContents();
        this.inv2 = pl.getInventory().getContents();
        this.loc = p.getLocation();
        this.loc2 = pl.getLocation();
        p.teleport(this.spawn);
        pl.teleport(this.spawn2);
        for (final PotionEffect pot : p.getActivePotionEffects()) {
            p.removePotionEffect(pot.getType());
        }
        for (final PotionEffect pot : pl.getActivePotionEffects()) {
            pl.removePotionEffect(pot.getType());
        }
        p.setMaxHealth(20.0);
        pl.setMaxHealth(20.0);
        p.setHealth(20.0);
        pl.setHealth(20.0);
        for (final Integer i : InvEvents.left) {
            if (inv.getItem((int)i) != null) {
                this.wager.add(inv.getItem((int)i));
            }
        }
        for (final Integer i : InvEvents.right) {
            if (inv.getItem((int)i) != null) {
                this.wager.add(inv.getItem((int)i));
            }
        }
        p.removeMetadata("duel", (Plugin)Duel.plugin);
        pl.removeMetadata("duel", (Plugin)Duel.plugin);
        p.setMetadata("no_drop_duel", (MetadataValue)new FixedMetadataValue((Plugin)Duel.plugin, (Object)true));
        pl.setMetadata("no_drop_duel", (MetadataValue)new FixedMetadataValue((Plugin)Duel.plugin, (Object)true));
        p.closeInventory();
        pl.closeInventory();
        PlayerEvents.noMove.add(p);
        PlayerEvents.noMove.add(pl);
        new BukkitRunnable() {
            int i = 5;
            
            public void run() {
                if (!p.hasMetadata("no_drop_duel")) {
                    PlayerEvents.noMove.remove(p);
                    PlayerEvents.noMove.remove(pl);
                    this.cancel();
                    return;
                }
                if (this.i <= 0) {
                    PlayerEvents.noMove.remove(p);
                    PlayerEvents.noMove.remove(pl);
                    p.sendMessage(String.valueOf(Duel.s) + "§6Duel §bstarted!");
                    pl.sendMessage(String.valueOf(Duel.s) + "§6Duel §bstarted!");
                    this.cancel();
                    return;
                }
                if (this.i == 1) {
                    p.sendMessage(String.valueOf(Duel.s) + "§6Duel §7starting in §b§n1§7 second");
                    pl.sendMessage(String.valueOf(Duel.s) + "§6Duel §7starting in §b§n1§7 second");
                }
                else if (this.i <= 3) {
                    p.sendMessage(String.valueOf(Duel.s) + "§6Duel §7starting in §b§n" + this.i + "§7 seconds");
                    pl.sendMessage(String.valueOf(Duel.s) + "§6Duel §7starting in §b§n" + this.i + "§7 seconds");
                }
                --this.i;
            }
        }.runTaskTimer((Plugin)Duel.plugin, 20L, 20L);
        Duel.plugin.getServer().broadcastMessage(String.valueOf(Duel.s) + "§7A §6Duel §7has started between §e" + p.getName() + " §7and §e" + pl.getName());
    }
    
    public void finish(final Player winner) {
        new BukkitRunnable() {
            boolean done = false;
            boolean done2 = false;
            
            public void run() {
                if (Arena.this.player != null && !Arena.this.player.isDead() && !this.done) {
                    Arena.this.player.getInventory().setArmorContents(Arena.this.armor);
                    Arena.this.player.getInventory().setContents(Arena.this.inv);
                    Arena.this.player.teleport(Arena.this.loc);
                    for (final PotionEffect pot : Arena.this.player.getActivePotionEffects()) {
                        Arena.this.player.removePotionEffect(pot.getType());
                    }
                    Arena.this.player.setMaxHealth(20.0);
                    Arena.this.player.setHealth(20.0);
                    if (winner != null && winner.getUniqueId().equals(Arena.this.player.getUniqueId())) {
                        for (final ItemStack i : Arena.this.wager) {
                            if (i != null) {
                                if (Arena.this.player.getInventory().firstEmpty() != -1) {
                                    Arena.this.player.getInventory().addItem(new ItemStack[] { i });
                                }
                                else {
                                    Arena.this.player.getWorld().dropItemNaturally(Arena.this.player.getLocation(), i);
                                }
                            }
                        }
                        Arena.access$5(Arena.this, new ArrayList());
                    }
                    Arena.access$6(Arena.this, null);
                    Arena.access$7(Arena.this, null);
                    Arena.access$8(Arena.this, null);
                    Arena.access$9(Arena.this, null);
                    this.done = true;
                }
                else if (Arena.this.player == null) {
                    this.done = true;
                }
                if (Arena.this.player2 != null && !Arena.this.player2.isDead() && !this.done2) {
                    Arena.this.player2.getInventory().setArmorContents(Arena.this.armor2);
                    Arena.this.player2.getInventory().setContents(Arena.this.inv2);
                    Arena.this.player2.teleport(Arena.this.loc2);
                    for (final PotionEffect pot : Arena.this.player2.getActivePotionEffects()) {
                        Arena.this.player2.removePotionEffect(pot.getType());
                    }
                    Arena.this.player2.setMaxHealth(20.0);
                    Arena.this.player2.setHealth(20.0);
                    if (winner != null && winner.getUniqueId().equals(Arena.this.player2.getUniqueId())) {
                        for (final ItemStack i : Arena.this.wager) {
                            if (i != null) {
                                if (Arena.this.player2.getInventory().firstEmpty() != -1) {
                                    Arena.this.player2.getInventory().addItem(new ItemStack[] { i });
                                }
                                else {
                                    Arena.this.player2.getWorld().dropItemNaturally(Arena.this.player2.getLocation(), i);
                                }
                            }
                        }
                        Arena.access$5(Arena.this, new ArrayList());
                    }
                    Arena.access$14(Arena.this, null);
                    Arena.access$15(Arena.this, null);
                    Arena.access$16(Arena.this, null);
                    Arena.access$17(Arena.this, null);
                    this.done2 = true;
                }
                else if (Arena.this.player2 == null) {
                    this.done2 = true;
                }
                if (this.done && this.done2) {
                    Arena.access$18(Arena.this, false);
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)Duel.plugin, 0L, 1L);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Location getSpawn() {
        return this.spawn;
    }
    
    public void setSpawn(final Location spawn) {
        this.spawn = spawn;
    }
    
    public Location getSpawn2() {
        return this.spawn2;
    }
    
    public void setSpawn2(final Location spawn2) {
        this.spawn2 = spawn2;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void setPlayer(final Player player) {
        this.player = player;
    }
    
    public Player getPlayer2() {
        return this.player2;
    }
    
    public void setPlayer2(final Player player2) {
        this.player2 = player2;
    }
    
    public ItemStack[] getInv() {
        return this.inv;
    }
    
    public void setInv(final ItemStack[] inv) {
        this.inv = inv;
    }
    
    public ItemStack[] getInv2() {
        return this.inv2;
    }
    
    public void setInv2(final ItemStack[] inv2) {
        this.inv2 = inv2;
    }
    
    public ItemStack[] getArmor() {
        return this.armor;
    }
    
    public void setArmor(final ItemStack[] armor) {
        this.armor = armor;
    }
    
    public ItemStack[] getArmor2() {
        return this.armor2;
    }
    
    public void setArmor2(final ItemStack[] armor2) {
        this.armor2 = armor2;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public void setLoc(final Location loc) {
        this.loc = loc;
    }
    
    public Location getLoc2() {
        return this.loc2;
    }
    
    public void setLoc2(final Location loc2) {
        this.loc2 = loc2;
    }
    
    public List<ItemStack> getWager() {
        return this.wager;
    }
    
    public void addWager(final ItemStack wager) {
        this.wager.add(wager);
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void setRunning(final boolean running) {
        this.running = running;
    }
    
    static /* synthetic */ void access$5(final Arena arena, final List wager) {
        arena.wager = (List<ItemStack>)wager;
    }
    
    static /* synthetic */ void access$6(final Arena arena, final Player player) {
        arena.player = player;
    }
    
    static /* synthetic */ void access$7(final Arena arena, final ItemStack[] inv) {
        arena.inv = inv;
    }
    
    static /* synthetic */ void access$8(final Arena arena, final ItemStack[] armor) {
        arena.armor = armor;
    }
    
    static /* synthetic */ void access$9(final Arena arena, final Location loc) {
        arena.loc = loc;
    }
    
    static /* synthetic */ void access$14(final Arena arena, final Player player2) {
        arena.player2 = player2;
    }
    
    static /* synthetic */ void access$15(final Arena arena, final ItemStack[] inv2) {
        arena.inv2 = inv2;
    }
    
    static /* synthetic */ void access$16(final Arena arena, final ItemStack[] armor2) {
        arena.armor2 = armor2;
    }
    
    static /* synthetic */ void access$17(final Arena arena, final Location loc2) {
        arena.loc2 = loc2;
    }
    
    static /* synthetic */ void access$18(final Arena arena, final boolean running) {
        arena.running = running;
    }
}

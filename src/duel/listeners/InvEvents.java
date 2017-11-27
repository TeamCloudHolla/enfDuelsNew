package duel;

import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import duel.objects.*;
import net.milkbowl.vault.economy.*;
import com.google.common.collect.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import duel.listeners.*;
import org.bukkit.enchantments.*;
import java.io.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.metadata.*;
import duel.utils.*;
import org.bukkit.inventory.*;

public class Duel extends JavaPlugin
{
    File file;
    YamlConfiguration storage;
    public static Map<Player, Map<Player, String>> request;
    public static List<Arena> arenas;
    public static ItemStack[] iron;
    public static ItemStack[] gapple;
    public static ItemStack[] potion;
    public static String s;
    public static Economy econ;
    public static Duel plugin;
    
    static {
        Duel.request = (Map<Player, Map<Player, String>>)Maps.newHashMap();
        Duel.arenas = new ArrayList<Arena>();
        Duel.iron = new ItemStack[36];
        Duel.gapple = new ItemStack[36];
        Duel.potion = new ItemStack[36];
        Duel.s = "§a* ";
        Duel.econ = null;
    }
    
    public Duel() {
        this.file = new File(this.getDataFolder(), "Arenas.yml");
        this.storage = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void onEnable() {
        Duel.plugin = this;
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerEvents(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InvEvents(), (Plugin)this);
        this.storage = Util.getConfiguration((Plugin)this, "Arenas.yml");
        if ((Duel.econ = Util.setupEconomy((Plugin)this)) == null) {
            this.getServer().getConsoleSender().sendMessage(String.valueOf(Duel.s) + "§c§lSetupEconomy FAILED -- You need Vault AND an Economy Plugin for this plugin to work");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
        ConfigUtils.loadArenas((Plugin)this, this.storage);
        final ItemStack isword = new ItemStack(Material.IRON_SWORD);
        final ItemStack ihelm = new ItemStack(Material.IRON_HELMET);
        final ItemStack ichest = new ItemStack(Material.IRON_CHESTPLATE);
        final ItemStack ilegs = new ItemStack(Material.IRON_LEGGINGS);
        final ItemStack iboots = new ItemStack(Material.IRON_BOOTS);
        final ItemStack bow = new ItemStack(Material.BOW);
        final ItemStack dsword = new ItemStack(Material.DIAMOND_SWORD);
        final ItemStack dhelm = new ItemStack(Material.DIAMOND_HELMET);
        final ItemStack dchest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        final ItemStack dlegs = new ItemStack(Material.DIAMOND_LEGGINGS);
        final ItemStack dboots = new ItemStack(Material.DIAMOND_BOOTS);
        isword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ihelm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ichest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ilegs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        iboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ihelm.addEnchantment(Enchantment.DURABILITY, 3);
        ichest.addEnchantment(Enchantment.DURABILITY, 3);
        ilegs.addEnchantment(Enchantment.DURABILITY, 3);
        iboots.addEnchantment(Enchantment.DURABILITY, 3);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
        dsword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        dhelm.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        dchest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        dlegs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        dboots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        dhelm.addEnchantment(Enchantment.DURABILITY, 3);
        dchest.addEnchantment(Enchantment.DURABILITY, 3);
        dlegs.addEnchantment(Enchantment.DURABILITY, 3);
        dboots.addEnchantment(Enchantment.DURABILITY, 3);
        Duel.iron[0] = isword;
        Duel.iron[1] = new ItemStack(Material.GOLDEN_APPLE, 5);
        Duel.iron[2] = new ItemStack(Material.BOW);
        Duel.iron[3] = ihelm;
        Duel.iron[4] = ichest;
        Duel.iron[5] = ilegs;
        Duel.iron[6] = iboots;
        Duel.iron[8] = new ItemStack(Material.COOKED_BEEF, 32);
        Duel.iron[29] = new ItemStack(Material.ARROW, 32);
        Duel.gapple[0] = dsword;
        Duel.gapple[1] = new ItemStack(Material.GOLDEN_APPLE, 5, (short)1);
        Duel.gapple[2] = bow;
        Duel.gapple[3] = dhelm;
        Duel.gapple[4] = dchest;
        Duel.gapple[5] = dlegs;
        Duel.gapple[6] = dboots;
        Duel.gapple[8] = new ItemStack(Material.COOKED_BEEF, 32);
        Duel.gapple[29] = new ItemStack(Material.ARROW, 64);
        Duel.gapple[7] = new ItemStack(Material.POTION, 1, (short)8201);
        Duel.gapple[34] = new ItemStack(Material.POTION, 1, (short)8194);
        Duel.potion[0] = dsword;
        Duel.potion[1] = bow;
        Duel.potion[2] = dhelm;
        Duel.potion[3] = dchest;
        Duel.potion[4] = dlegs;
        Duel.potion[5] = dboots;
        Duel.potion[6] = new ItemStack(Material.POTION, 1, (short)16457);
        Duel.potion[7] = new ItemStack(Material.POTION, 1, (short)16450);
        Duel.potion[8] = new ItemStack(Material.COOKED_BEEF, 32);
        Duel.potion[9] = new ItemStack(Material.ARROW, 64);
        for (int i = 10; i < 32; ++i) {
            Duel.potion[i] = new ItemStack(Material.POTION, 1, (short)16421);
        }
        Duel.potion[32] = new ItemStack(Material.POTION, 1, (short)16457);
        Duel.potion[33] = new ItemStack(Material.POTION, 1, (short)16457);
        Duel.potion[34] = new ItemStack(Material.POTION, 1, (short)16450);
        Duel.potion[35] = new ItemStack(Material.POTION, 1, (short)16450);
    }
    
    public void onDisable() {
        Duel.plugin = null;
        if (this.storage.isConfigurationSection("Arenas")) {
            for (final String s : this.storage.getConfigurationSection("Arenas").getValues(false).keySet()) {
                this.storage.set("Arenas." + s, (Object)null);
            }
        }
        if (this.storage.isConfigurationSection("Kills")) {
            for (final String s : this.storage.getConfigurationSection("Kills").getValues(false).keySet()) {
                this.storage.set("Kills." + s, (Object)null);
            }
        }
        if (this.storage.isConfigurationSection("Deaths")) {
            for (final String s : this.storage.getConfigurationSection("Deaths").getValues(false).keySet()) {
                this.storage.set("Deaths." + s, (Object)null);
            }
        }
        if (this.storage.isConfigurationSection("Won")) {
            for (final String s : this.storage.getConfigurationSection("Won").getValues(false).keySet()) {
                this.storage.set("Won." + s, (Object)null);
            }
        }
        if (this.storage.isConfigurationSection("Lost")) {
            for (final String s : this.storage.getConfigurationSection("Lost").getValues(false).keySet()) {
                this.storage.set("Lost." + s, (Object)null);
            }
        }
        try {
            for (final Arena a : Duel.arenas) {
                final String s2 = a.getName();
                if (a.getSpawn() != null) {
                    this.storage.set("Arenas." + s2 + ".Spawn.World", (Object)a.getSpawn().getWorld().getName());
                    this.storage.set("Arenas." + s2 + ".Spawn.X", (Object)a.getSpawn().getX());
                    this.storage.set("Arenas." + s2 + ".Spawn.Y", (Object)a.getSpawn().getY());
                    this.storage.set("Arenas." + s2 + ".Spawn.Z", (Object)a.getSpawn().getZ());
                    this.storage.set("Arenas." + s2 + ".Spawn.Pitch", (Object)a.getSpawn().getPitch());
                    this.storage.set("Arenas." + s2 + ".Spawn.Yaw", (Object)a.getSpawn().getYaw());
                }
                if (a.getSpawn2() != null) {
                    this.storage.set("Arenas." + s2 + ".Spawn2.World", (Object)a.getSpawn2().getWorld().getName());
                    this.storage.set("Arenas." + s2 + ".Spawn2.X", (Object)a.getSpawn2().getX());
                    this.storage.set("Arenas." + s2 + ".Spawn2.Y", (Object)a.getSpawn2().getY());
                    this.storage.set("Arenas." + s2 + ".Spawn2.Z", (Object)a.getSpawn2().getZ());
                    this.storage.set("Arenas." + s2 + ".Spawn2.Pitch", (Object)a.getSpawn2().getPitch());
                    this.storage.set("Arenas." + s2 + ".Spawn2.Yaw", (Object)a.getSpawn2().getYaw());
                }
            }
            this.storage.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final Arena a : Duel.arenas) {
            if (a.isRunning()) {
                a.finish(null);
            }
        }
        for (final Player p : this.getServer().getOnlinePlayers()) {
            if (p.hasMetadata("duel")) {
                for (final HumanEntity pl : p.getOpenInventory().getTopInventory().getViewers()) {
                    pl.closeInventory();
                    pl.removeMetadata("duel", (Plugin)this);
                }
            }
        }
        Duel.request = null;
        Duel.arenas = null;
        Duel.iron = null;
        Duel.gapple = null;
        Duel.potion = null;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (label.equalsIgnoreCase("duel") && sender instanceof Player) {
            final Player p = (Player)sender;
            if (args.length == 2 && (args[0].equalsIgnoreCase("send") || args[0].equalsIgnoreCase("request"))) {
                if (this.getServer().getPlayer(args[1]) != null) {
                    final Player pl = this.getServer().getPlayer(args[1]);
                    if (pl.equals(p)) {
                        p.sendMessage(String.valueOf(Duel.s) + "§7You can't duel yourself!");
                        return true;
                    }
                    if (!Duel.request.containsKey(p)) {
                        Duel.request.put(p, new HashMap<Player, String>());
                    }
                    if (!Duel.request.get(p).containsKey(pl)) {
                        final Inventory inv = Bukkit.createInventory((InventoryHolder)p, 45, "§2Duel Request");
                        for (int i = 0; i < 10; ++i) {
                            inv.setItem(i, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        }
                        inv.setItem(17, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        inv.setItem(18, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        inv.setItem(26, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        inv.setItem(27, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        for (int i = 35; i < 45; ++i) {
                            inv.setItem(i, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        }
                        inv.setItem(4, Util.createHead(args[1], "§e" + args[1], "§7You are requesting to duel this player"));
                        inv.setItem(20, Util.createItem(Material.IRON_SWORD, "§f§lIron PvP", "§7PvP with an Iron kit"));
                        inv.setItem(22, Util.createItem(Material.GOLDEN_APPLE, 1, 1, "§6§lGapple PvP", "§7PvP with Diamond Gear and God Apples"));
                        inv.setItem(24, Util.createItem(Material.POTION, 1, 8233, "§5§lPotion PvP", "§7PvP with Diamond Gear and Potions"));
                        inv.setItem(40, Util.createItem(Material.NETHER_STAR, "§cClose Inventory", "§7Cancel your Duel request"));
                        p.openInventory(inv);
                    }
                    else {
                        p.sendMessage(String.valueOf(Duel.s) + "§7You have already requested to duel that player");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Duel.s) + "§7That player is not online");
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
                if (this.getServer().getPlayer(args[1]) != null) {
                    final Player pl = this.getServer().getPlayer(args[1]);
                    if (Duel.request.containsKey(pl) && Duel.request.get(pl).containsKey(p)) {
                        if (p.hasMetadata("duel") || pl.hasMetadata("duel")) {
                            p.sendMessage(String.valueOf(Duel.s) + "§7That player is already dueling someone");
                            return true;
                        }
                        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, "§a§lWagers");
                        inv.setItem(3, Util.createItem(Material.STAINED_GLASS_PANE, 1, 1, "§aAccept Wager", "§7Click to accept wager"));
                        inv.setItem(4, Util.createItem(Material.STAINED_GLASS_PANE, 1, 14, "§cDeny Wager", "§7Click to close Inventory"));
                        inv.setItem(5, Util.createItem(Material.STAINED_GLASS_PANE, 1, 1, "§aAccept Wager", "§7Click to accept wager"));
                        for (int i = 13; i < 36; i += 9) {
                            inv.setItem(i, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
                        }
                        p.setMetadata("duel", (MetadataValue)new FixedMetadataValue((Plugin)this, (Object)"left"));
                        pl.setMetadata("duel", (MetadataValue)new FixedMetadataValue((Plugin)this, (Object)"right"));
                        p.openInventory(inv);
                        pl.openInventory(inv);
                    }
                    else {
                        p.sendMessage(String.valueOf(Duel.s) + "§7That player has not requested you to duel!");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Duel.s) + "§7That player is not online");
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("deny")) {
                if (this.getServer().getPlayer(args[1]) != null) {
                    final Player pl = this.getServer().getPlayer(args[1]);
                    if (Duel.request.containsKey(pl) && Duel.request.get(pl).containsKey(p)) {
                        Duel.request.get(pl).remove(p);
                        p.sendMessage(String.valueOf(Duel.s) + "§7You have denied §e" + pl.getName() + "'s §6Duel §7request");
                        pl.sendMessage(String.valueOf(Duel.s) + "§e" + p.getName() + " §7has denied your §6Duel §7request");
                    }
                    else {
                        p.sendMessage(String.valueOf(Duel.s) + "§7That player has not requested you to duel!");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Duel.s) + "§7That player is not online");
                }
            }
            else if (args.length == 1 && this.getServer().getPlayer(args[0]) != null) {
                p.performCommand("duel send " + args[0]);
            }
            else if (args.length == 1 && (args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("leave"))) {
                if (p.hasMetadata("no_drop_duel")) {
                    Duel.request.remove(p);
                    for (final Player pl : Duel.request.keySet()) {
                        Duel.request.get(pl).remove(p);
                    }
                    for (final Arena a : Duel.arenas) {
                        if (a.isRunning() && (a.getPlayer().equals(p) || a.getPlayer2().equals(p))) {
                            final Player k = a.getPlayer().equals(p) ? a.getPlayer2() : a.getPlayer();
                            p.removeMetadata("no_drop_duel", (Plugin)this);
                            k.removeMetadata("no_drop_duel", (Plugin)this);
                            a.finish(k);
                            this.getServer().broadcastMessage(String.valueOf(Duel.s) + "§e" + k.getName() + " §7beat §e" + p.getName() + " §7in the duel arena!");
                            break;
                        }
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Duel.s) + "§7You are not in a Duel");
                }
            }
            else {
                p.sendMessage("§7<§o------------§7>§6§l Duel Help §7<§o------------§7>");
                p.sendMessage("§6/duel [request/send] <player> §8- §7Request a player to Duel");
                p.sendMessage("§6/duel accept <player> §8- §7Accept a player's Duel request");
                p.sendMessage("§6/duel deny <player> §8- §7Deny a player's Duel request");
                p.sendMessage("§6/duel leave/quit §8- §7Leave the duel you are in");
            }
        }
        else if (label.equalsIgnoreCase("arena") && (sender.isOp() || sender.getName().equals("Creeperzombi3"))) {
            if (args.length == 2 && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add"))) {
                Duel.arenas.add(new Arena(args[1]));
                DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§7Created an Arena named §b" + args[1]);
                DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cUse §7/arena setspawn <arena> §cand §7/arena setspawn2 <arena> §cto complete the arena");
            }
            else if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete"))) {
                if (DuelUtils.getArena(args[1]) != null) {
                    Duel.arenas.remove(DuelUtils.getArena(args[1]));
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§7Deleted the Arena named §b" + args[1]);
                }
                else {
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§7There is no arena with that name");
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                DuelUtils.send((Plugin)this, sender, "§7<§o------------§7>§a§l Arenas §7<§o------------§7>");
                for (final Arena a2 : Duel.arenas) {
                    DuelUtils.send((Plugin)this, sender, "§b" + a2.getName());
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
                if (DuelUtils.getArena(args[1]) != null) {
                    if (sender instanceof Player) {
                        final Player p = (Player)sender;
                        DuelUtils.getArena(args[1]).setSpawn(p.getLocation());
                        p.sendMessage(String.valueOf(Duel.s) + "§7You have set your current location as §b" + args[1] + "'s §7Spawn");
                    }
                    else {
                        DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cYou have no location!");
                    }
                }
                else {
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cThere is no arena with that name");
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("setspawn2")) {
                if (DuelUtils.getArena(args[1]) != null) {
                    if (sender instanceof Player) {
                        final Player p = (Player)sender;
                        DuelUtils.getArena(args[1]).setSpawn2(p.getLocation());
                        p.sendMessage(String.valueOf(Duel.s) + "§7You have set your current location as §b" + args[1] + "'s §7Spawn2");
                    }
                    else {
                        DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cYou have no location!");
                    }
                }
                else {
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cThere is no arena with that name");
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
                if (DuelUtils.getArena(args[1]) != null) {
                    final Arena a2 = DuelUtils.getArena(args[1]);
                    DuelUtils.send((Plugin)this, sender, "§7<§o------------§7>§a§l Arena Info §7<§o------------§7>");
                    DuelUtils.send((Plugin)this, sender, "§eName: §9" + a2.getName());
                    DuelUtils.sendArenaLocationInfo((Plugin)this, sender, a2);
                    DuelUtils.send((Plugin)this, sender, "§eCurrently Running: §9" + a2.isRunning());
                    if (a2.isRunning()) {
                        DuelUtils.send((Plugin)this, sender, "§ePlayer 1: §9" + a2.getPlayer().getName());
                        DuelUtils.send((Plugin)this, sender, "§ePlayer 2: §9" + a2.getPlayer2().getName());
                    }
                }
                else {
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cThere is no arena with that name");
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("end")) {
                if (DuelUtils.getArena(args[1]) != null) {
                    final Arena a2 = DuelUtils.getArena(args[1]);
                    if (a2.isRunning()) {
                        a2.finish(null);
                        this.getServer().broadcastMessage(String.valueOf(Duel.s) + "§7Arena §e" + a2.getName() + " §7was ended.");
                    }
                    else {
                        DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§7That arena is not currently running");
                    }
                }
                else {
                    DuelUtils.send((Plugin)this, sender, String.valueOf(Duel.s) + "§cThere is no arena with that name");
                }
            }
            else {
                DuelUtils.send((Plugin)this, sender, "§7<§o------------§7>§a§l Arena Help §7<§o------------§7>");
                DuelUtils.send((Plugin)this, sender, "§9/arena list §8- §7List the Arenas");
                DuelUtils.send((Plugin)this, sender, "§9/arena create <arena> §8- §7Create an Arena");
                DuelUtils.send((Plugin)this, sender, "§9/arena remove <arena> §8- §7Delete an Arena");
                DuelUtils.send((Plugin)this, sender, "§9/arena setspawn <arena> §8- §7Set a spawn point");
                DuelUtils.send((Plugin)this, sender, "§9/arena setspawn2 <arena> §8- §7Set a spawn point");
                DuelUtils.send((Plugin)this, sender, "§9/arena info <arena> §8- §7Get the Arena information");
                DuelUtils.send((Plugin)this, sender, "§9/arena end <arena> §8- §7End the Arena battle");
            }
        }
        return true;
    }
}

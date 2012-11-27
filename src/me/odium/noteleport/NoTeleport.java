package me.odium.noteleport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.odium.noteleport.commands.bring;
import me.odium.noteleport.commands.call;
import me.odium.noteleport.commands.notp;
import me.odium.noteleport.commands.put;
import me.odium.noteleport.commands.teleport;
import me.odium.noteleport.commands.tpaccept;
import me.odium.noteleport.commands.tpdeny;
import me.odium.noteleport.listeners.PListener;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoTeleport extends JavaPlugin {
  public Logger log = Logger.getLogger("Minecraft");

  public ConcurrentHashMap<Player, List<Player>> CallMap = new ConcurrentHashMap<Player, List<Player>>();

  //Custom Config  
  private FileConfiguration StorageConfig = null;
  private File StorageConfigFile = null;

  public void reloadStorageConfig() {
    if (StorageConfigFile == null) {
      StorageConfigFile = new File(getDataFolder(), "StorageConfig.yml");
    }
    StorageConfig = YamlConfiguration.loadConfiguration(StorageConfigFile);

    // Look for defaults in the jar
    InputStream defConfigStream = getResource("StorageConfig.yml");
    if (defConfigStream != null) {
      YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
      StorageConfig.setDefaults(defConfig);
    }
  }
  public FileConfiguration getStorageConfig() {
    if (StorageConfig == null) {
      reloadStorageConfig();
    }
    return StorageConfig;
  }

  public void saveStorageConfig() {
    if (StorageConfig == null || StorageConfigFile == null) {
      return;
    }
    try {
      StorageConfig.save(StorageConfigFile);
    } catch (IOException ex) {
      Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + StorageConfigFile, ex);
    }
  }
  // End Custom Config


  public void onEnable(){    
    log.info("[" + "Simple-NoTeleport" + "] " + getDescription().getVersion() + " enabled.");
    // Load Config.yml
    FileConfiguration cfg = getConfig();
    FileConfigurationOptions cfgOptions = cfg.options();
    cfgOptions.copyDefaults(true);
    cfgOptions.copyHeader(true);
    saveConfig();
    // Load Custom Config
    FileConfiguration ccfg = getStorageConfig();
    FileConfigurationOptions ccfgOptions = ccfg.options();
    ccfgOptions.copyDefaults(true);
    ccfgOptions.copyHeader(true);
    saveStorageConfig();
    // declare new listener
    new PListener(this);
    // Declare Command Executor
    this.getCommand("notp").setExecutor(new notp(this));
    this.getCommand("call").setExecutor(new call(this));
    this.getCommand("tpaccept").setExecutor(new tpaccept(this));
    this.getCommand("tpdeny").setExecutor(new tpdeny(this));
    this.getCommand("teleport").setExecutor(new teleport(this));
    this.getCommand("bring").setExecutor(new bring(this));
    this.getCommand("put").setExecutor(new put(this));
  }

  public void onDisable(){ 
    log.info("[" + "Simple-NoTeleport" + "] " + getDescription().getVersion() + " disabled.");    
  }  

  public void displayHelp(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD+"[ "+ChatColor.DARK_RED+"NoTeleport "+getDescription().getVersion()+ChatColor.GOLD+" ]");
    sender.sendMessage(ChatColor.ITALIC+"() = Required | [] = Optional");
    sender.sendMessage(ChatColor.YELLOW+" /NoTP on/off "+ChatColor.WHITE+"- Toggle user's ability to teleport to you");
    sender.sendMessage(ChatColor.YELLOW+" /NoTP add/remove "+ChatColor.WHITE+"- Edit list of users allowed to tp to you");
    sender.sendMessage(ChatColor.YELLOW+" /NoTP access "+ChatColor.WHITE+"- List users with tp access to you");
    sender.sendMessage(" - ");    
    sender.sendMessage(ChatColor.YELLOW+" /call <player> "+ChatColor.WHITE+"- Send a teleport request");
    sender.sendMessage(ChatColor.YELLOW+" /tpaccept "+ChatColor.WHITE+"- Accept a teleport request");
    sender.sendMessage(ChatColor.YELLOW+" /tpdeny "+ChatColor.WHITE+"- Deny a teleport request");
    sender.sendMessage(" - ");
    sender.sendMessage(ChatColor.YELLOW+" /teleport <player> [player] "+ChatColor.WHITE+"- Teleport to a player");
    if (sender == null || sender.hasPermission("notp.bring")) {
      sender.sendMessage(ChatColor.YELLOW+" /bring <player> "+ChatColor.WHITE+"- Bring a player");
    }
    if (sender == null || sender.hasPermission("notp.put")) {
      sender.sendMessage(ChatColor.YELLOW+" /put [player] "+ChatColor.WHITE+"- Put a player");    
    }
    if (sender == null || sender.hasPermission("notp.reload")) {
      sender.sendMessage(ChatColor.YELLOW+" /NoTP reload "+ChatColor.WHITE+"- Reload config");
    }
  }

  public void output(CommandSender sender, String phrase, String playerName) {
    String prefix = ChatColor.GOLD+"* ";

    if (phrase == "NoPermission") {
      sender.sendMessage(prefix+ChatColor.RED+"You do not have permission.");
    }
    if (phrase == "OnlyByPlayer") {
      sender.sendMessage(prefix+ChatColor.RED+"This command can only be executed by a player");
    }
    if (phrase =="TeleportDenied") {
      sender.sendMessage(prefix+playerName+ChatColor.RED+" has denied teleporting");
    }
    if (phrase =="NoTeleportENABLED") {
      sender.sendMessage(prefix+ChatColor.GREEN+"You have successfully disabled teleporting");
    }
    if (phrase =="NoTeleportDISABLED") {
      sender.sendMessage(prefix+ChatColor.GREEN+"You have re-enabled teleporting");
    }
    if (phrase =="NoTeleportAlreadyENABLED") {
      sender.sendMessage(prefix+ChatColor.RED+"Teleporting already disabled");
    }
    if (phrase =="NoTeleportAlreadyDISABLED") {
      sender.sendMessage(prefix+ChatColor.RED+"Teleporting already enabled");
    }
    if (phrase =="AddedToAccess") {
      sender.sendMessage(prefix+ChatColor.YELLOW+playerName+" added to access list");
    }
    if (phrase =="RemovedFromAccess") {
      sender.sendMessage(prefix+ChatColor.YELLOW+playerName+" removed from access list");
    }
    if (phrase =="ArleadyInAccess") {
      sender.sendMessage(prefix+ChatColor.RED+playerName+" already in access list");
    }
    if (phrase =="NotInAccess") {
      sender.sendMessage(prefix+ChatColor.RED+playerName+" not in access list");
    }
    if (phrase =="PlayerNotOnline") {
      sender.sendMessage(prefix+ChatColor.RED+playerName+" not online");
    }
    if (phrase =="RequestReceived") {
      sender.sendMessage(prefix+ChatColor.YELLOW+playerName+" has sent you a teleport request (tpaccept / tpdeny)");
    }
    if (phrase =="RequestSent") {
      sender.sendMessage(prefix+ChatColor.YELLOW+"Teleport request sent to "+playerName);
    }
    if (phrase =="NoActiveRequests") {
      sender.sendMessage(prefix+ChatColor.RED+"No Active TP Requests");
    }
    if (phrase =="RequestAccepted") {
      sender.sendMessage(prefix+ChatColor.YELLOW+"Teleport request Accepted");
    }
    if (phrase =="RequestDenied") {
      sender.sendMessage(prefix+ChatColor.RED+"Teleport request Denied");
    }
    if (phrase =="TeleportedBy") {
      sender.sendMessage(prefix+ChatColor.YELLOW+"You've been teleported by "+playerName);
    }
    if (phrase =="YouTeleported") {
      sender.sendMessage(prefix+ChatColor.YELLOW+"You teleported "+playerName);
    }
    if (phrase =="Teleported") {
      sender.sendMessage(prefix+ChatColor.YELLOW+"Teleported.");
    }
    if (phrase =="HasNotSentCall") {
      sender.sendMessage(prefix+ChatColor.RED+playerName+" has not sent a call");
    }
  }

  public String myGetPlayerName(String name) { 
    Player caddPlayer = getServer().getPlayerExact(name);
    String pName;
    if(caddPlayer == null) {
      caddPlayer = getServer().getPlayer(name);
      if(caddPlayer == null) {
        pName = name;
      } else {
        pName = caddPlayer.getName();
      }
    } else {
      pName = caddPlayer.getName();
    }
    return pName;
  }



}


package me.odium.noteleport.commands;
import java.util.List;

import me.odium.noteleport.NoTeleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.entity.Player;

public class notp implements CommandExecutor {   

  public NoTeleport plugin;
  public notp(NoTeleport plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {    
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    if (args.length == 0 && player == null) {
      plugin.displayHelp(sender);
      return true;
    } else if (args.length != 0 && player == null) {
      plugin.output(sender, "OnlyByPlayer", "");
      return true;
    }
    
    String playerName = player.getName().toLowerCase();
    
    if (args.length == 0) {
      plugin.displayHelp(sender);
      return true;
    } else if (args.length == 1 && args[0].equalsIgnoreCase("on")) { 
      // IF PLAYER ENTRY EXISTS
      if (plugin.getStorageConfig().getString(playerName) != null) {
        // PLAYER ENTRY HAS NOTP ENABLED
        if (plugin.getStorageConfig().getBoolean(playerName+".enabled")) {
          plugin.output(player, "NoTeleportAlreadyENABLED", "");
          return true;
        } else {
          plugin.getStorageConfig().set(playerName+".enabled", true);
          plugin.saveStorageConfig();

          plugin.output(player, "NoTeleportENABLED", "");
          return true;
        }
        //IF PLAYER ENTRY DOESN'T EXIST
      } else {
        plugin.getStorageConfig().set(playerName+".enabled", true);
        plugin.saveStorageConfig();

        plugin.output(player, "NoTeleportENABLED", "");
        return true;
      }

    } else if (args.length == 1 && args[0].equalsIgnoreCase("off")) {
      // IF PLAYER ENTRY EXISTS
      if (plugin.getStorageConfig().getString(playerName) != null) {
        // PLAYER ENTRY HAS NOTP DISABLED
        if (!plugin.getStorageConfig().getBoolean(playerName+".enabled")) {
          plugin.output(player, "NoTeleportAlreadyDISABLED", "");
          return true;
        } else {
          plugin.getStorageConfig().set(playerName+".enabled", false);
          plugin.saveStorageConfig();

          plugin.output(player, "NoTeleportDISABLED", "");
          return true;
        }
      } else {
        plugin.output(player, "NoTeleportAlreadyDISABLED", "");
        return true;
      }
    } else if (args.length == 1 && args[0].equalsIgnoreCase("access")) {
      // IF PLAYER ENTRY EXISTS
      if (plugin.getStorageConfig().getString(playerName) != null) {
        List<String> Access = plugin.getStorageConfig().getStringList(playerName+".access");
        sender.sendMessage(ChatColor.GOLD+"["+ChatColor.WHITE+" TP Access List "+ChatColor.GOLD+"]");
        for (String plname : Access) {          
          sender.sendMessage(ChatColor.GOLD+"- "+ChatColor.WHITE+plname);
        }
        return true;
      } else {
        sender.sendMessage("No Entries in Access List");
        return true;
      }
    } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
      String targetName = plugin.myGetPlayerName(args[1]).toLowerCase();

      if (plugin.getStorageConfig().getString(playerName) == null) {
        plugin.getStorageConfig().set(playerName+".enabled", false);
        List<String> access = plugin.getStorageConfig().getStringList(playerName+".access");
        access.add(targetName);
        plugin.getStorageConfig().set(playerName+".access", access);
        plugin.saveStorageConfig();
        plugin.output(player, "AddedToAccess", targetName);
        return true;
      } else {
        List<String> access = plugin.getStorageConfig().getStringList(playerName+".access");        
        if (access.contains(targetName)) {
          plugin.output(player, "AlreadyInAccess", targetName);
          return true;
        } else {
          access.add(targetName);
          plugin.getStorageConfig().set(playerName+".access", access);
          plugin.saveStorageConfig();
          plugin.output(player, "AddedToAccess", targetName);
          return true;
        }
      }
    } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
      String targetName = plugin.myGetPlayerName(args[1]).toLowerCase();

      if (plugin.getStorageConfig().getString(playerName) == null) {       
        plugin.output(player, "NotInAccess", targetName);
        return true;
      } else {
        List<String> access = plugin.getStorageConfig().getStringList(playerName+".access");        
        if (access.contains(targetName)) {
          access.remove(targetName);
          plugin.getStorageConfig().set(playerName+".access", access);
          plugin.saveStorageConfig();          
          plugin.output(player, "RemovedFromAccess", targetName);
          return true;
        } else {
          plugin.output(player, "NotInAccess", targetName);
          return true;
        }
      }
    }
    return false;
  }
}

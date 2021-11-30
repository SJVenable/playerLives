package me.sammy.playerLives;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;

public class LifeCommand implements CommandExecutor {

    public DataManager data;

    public LifeCommand(DataManager mainData) {
        this.data = mainData;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if(s.equalsIgnoreCase("addLives")) {

                Player player = Bukkit.getPlayer(strings[0]);
                if(player != null) {

                    int nowLives = data.getLives(player.getUniqueId()) + Integer.parseInt(strings[1]);
                    System.out.println("nowLives: " + nowLives);
                    System.out.println("id: " + player.getUniqueId());
                    if(nowLives > 5) {
                        data.setLives(player.getUniqueId(), 5);
                        player.sendMessage(ChatColor.RED + "You cannot have more than 5 lives");
                    }
                    else data.setLives(player.getUniqueId(), nowLives);
                    if(nowLives <= 0) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getDisplayName(), "You ran out of lives - get a friend to revive you or report this to admin if it was unfairly dealt", null, "Console");
                        player.kickPlayer("0 lives");
                    }
                }
                else commandSender.sendMessage("Please enter a player name");
                }
            data.saveConfig();
            }
        return false;
    }
}

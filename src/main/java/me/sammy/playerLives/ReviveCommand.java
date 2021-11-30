package me.sammy.playerLives;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReviveCommand implements CommandExecutor {
    public DataManager data;

    public ReviveCommand(DataManager mainData) {
        this.data = mainData;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (s.equalsIgnoreCase("revive")) {

                Player player = Bukkit.getPlayer(strings[0]);
                if (player != null) {

                    int nowLives = data.getLives(player.getUniqueId());
                    if (nowLives <= 0) {
                        Bukkit.getBanList(BanList.Type.NAME).pardon(player.getDisplayName());
                        data.setLives(player.getUniqueId(), 1);
                    }
                } else commandSender.sendMessage("Please enter a player name");
            }
            data.saveConfig();
        }
        return false;
    }
}






        /*if(s.equalsIgnoreCase("Revive")) {
            if(data.getConfig().getConfigurationSection("PlayerLives").contains(strings[0])) {
                Player player = Bukkit.getPlayer(strings[0]);
                data.setLives(player.getUniqueId(), 1);
                Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
                player.sendMessage("You have been revived by" + commandSender.toString() + " and have one life!");
                data.saveConfig();
            }
            else commandSender.sendMessage("Please enter a player name");
        }
        return false;
    }
}*/

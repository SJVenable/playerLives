package me.sammy.playerLives;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PlayerLives extends JavaPlugin implements Listener {

    public DataManager data;
    public static PlayerLives instance;

    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        data.saveDefaultConfig();
        data.getConfig().createSection("PlayerLives");
        data.saveConfig();
        Objects.requireNonNull(this.getCommand("AddCommand")).setExecutor(new lifeCommand(this.data));
        //line above gives an error
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(this, this);



    }

    @Override
    public void onDisable() {

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.getPlayer().setPlayerListName(ChatColor.YELLOW + e.getPlayer().getName() + " ( " + data.getLives(player.getUniqueId()) + " )");
        if (!Objects.requireNonNull(this.data.getConfig().getConfigurationSection("PlayerLives")).contains(player.getUniqueId().toString())) {
            Objects.requireNonNull(data.getConfig().getConfigurationSection("PlayerLives")).set(player.getUniqueId().toString(), 5);
        }
        int lives = data.getLives(e.getPlayer().getUniqueId());
        player.sendMessage("Welcome! You have " +  lives + " lives, use them wisely");
        data.saveConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        data.setLives(e.getEntity().getUniqueId(), this.data.getLives(e.getEntity().getUniqueId()) - 1);
        data.saveConfig();
    }

}

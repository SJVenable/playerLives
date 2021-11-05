package me.sammy.playerLives;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class DataManager {
    private final PlayerLives plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(PlayerLives plugin) {
        this.plugin = plugin;
        //saves / initialises config
        saveDefaultConfig();
    }
    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("data.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;
    }
    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {

            plugin.getLogger().log(Level.SEVERE, "Could not save config" + this.configFile, e);
        }
    }
    public void saveDefaultConfig() {
        if (this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "data.yml");

        if(!this.configFile.exists()){
            this.plugin.saveResource("data.yml", false);

        }
    }

    public void setLives(UUID id, int lives) {
        String pid = id.toString();
        Objects.requireNonNull(this.getConfig().getConfigurationSection("PlayerLives")).set(pid, lives);
        Bukkit.getPlayer(id).sendMessage(ChatColor.GREEN + "You have " + lives + " left");
        if(lives <= 0) {
            banPlayer(Objects.requireNonNull(Bukkit.getPlayer(id)), ChatColor.translateAlternateColorCodes('&', "&c&lYou were banned from the server for having no lives left! "));
            Objects.requireNonNull(Bukkit.getPlayer(id)).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&c&lYou were banned from the server for having less than 0 crypto! Join back in the next season!"));

            this.saveConfig();
        }
        this.saveConfig();
    }

    public int getLives(UUID id) {
        return Objects.requireNonNull(this.getConfig().getConfigurationSection("PlayerLives")).getInt(id.toString());
    }

    public void banPlayer(Player p, String reason) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(p.getPlayerListName(), reason, null, null);
        p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&c&lYou were banned from the server for having no lives left!"));

    }
}

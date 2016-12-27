package net.pl3x.bukkit.eggdrop.configuration;

import net.pl3x.bukkit.eggdrop.EggDrop;
import net.pl3x.bukkit.eggdrop.ItemUtil;
import net.pl3x.bukkit.eggdrop.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {
    public static boolean COLOR_LOGS = true;
    public static boolean DEBUG_MODE = false;
    public static String LANGUAGE_FILE = "lang-en.yml";
    public static ItemStack EGG_TOTEM;

    public static void reload() {
        EggDrop plugin = EggDrop.getPlugin();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        COLOR_LOGS = config.getBoolean("color-logs", true);
        DEBUG_MODE = config.getBoolean("debug-mode", false);
        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");

        EGG_TOTEM = ItemUtil.getItemStack(config.getConfigurationSection("egg-totem"));
        if (EGG_TOTEM == null) {
            Logger.error("Corrupt item config: egg-totem");
        }
    }
}

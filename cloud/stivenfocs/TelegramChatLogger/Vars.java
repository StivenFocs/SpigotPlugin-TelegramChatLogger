package cloud.stivenfocs.TelegramChatLogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Vars {

    public static Loader plugin;
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    static Vars vars;
    public static Vars getVars() {
        if (vars == null) {
            vars = new Vars();
        }

        return vars;
    }

    ///////////////////////////////

    public static Boolean debug = false;
    public static String bot_token = "";
    public static String chat_id = "";
    public static Boolean join_log_enabled = true;
    public static Boolean join_log_silenced = false;
    public static Boolean quit_log_enabled = true;
    public static Boolean quit_log_silenced = false;
    public static Boolean chat_log_enabled = true;
    public static Boolean chat_log_silenced = false;
    public static List<String> silenced_players = new ArrayList<>();
    public static List<String> ignored_players = new ArrayList<>();

    public static String player_joined = "";
    public static String player_left = "";
    public static String player_chat = "";
    public static String configuration_reloaded = "";
    public static String an_error_occurred = "";
    public static String unknown_subcommand = "";
    public static String no_permission = "";
    public static List<String> help_admin = new ArrayList<>();
    public static List<String> help_user = new ArrayList<>();

    ///////////////////////////////

    public boolean reloadVars() {
        try {
            plugin.reloadConfig();

            getConfig().options().copyDefaults(true);
            getConfig().options().header("Developed with LOV by StivenFocs");

            getConfig().addDefault("options.debug", false);
            getConfig().addDefault("options.bot_token", "abc");
            getConfig().addDefault("options.chat_id", 0);
            getConfig().addDefault("options.join_log.enabled", true);
            getConfig().addDefault("options.join_log.silenced", false);
            getConfig().addDefault("options.quit_log.enabled", true);
            getConfig().addDefault("options.quit_log.silenced", false);
            getConfig().addDefault("options.chat_log.enabled", true);
            getConfig().addDefault("options.chat_log.silenced", false);
            getConfig().addDefault("options.silenced_players", new ArrayList<>());
            getConfig().addDefault("options.ignored_players", new ArrayList<>());

            getConfig().addDefault("messages.player_joined", "%player% joined the game");
            getConfig().addDefault("messages.player_left", "%player% left the game");
            getConfig().addDefault("messages.player_chat", "%player% » %message%");
            getConfig().addDefault("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            getConfig().addDefault("messages.an_error_occurred", "&cAn error occurred with this task..");
            getConfig().addDefault("messages.unknown_subcommand", getConfig().getString("messages.unknown_subcommand", "&cUnknown subcommand"));
            getConfig().addDefault("messages.no_permission", "&cYou're not permitted to do this.");
            List<String> new_help_admin = new ArrayList<>();
            new_help_admin.add("&8&m==========================");
            new_help_admin.add("&7* &3Telegram&bChat&9Logger");
            new_help_admin.add("");
            new_help_admin.add("&7* /tglogger reload &8&m| &7Reload the configuration");
            new_help_admin.add("");
            new_help_admin.add("&8&m==========================");
            getConfig().addDefault("messages.help_admin", new_help_admin);
            List<String> new_help_user = new ArrayList<>();
            new_help_user.add("&8&m==========================");
            new_help_user.add("&7* &3Telegram&bChat&9Logger");
            new_help_user.add("");
            new_help_user.add("&7* Just a moderation tool.");
            new_help_user.add("");
            new_help_user.add("&8&m==========================");
            getConfig().addDefault("messages.help_user", new_help_user);

            plugin.saveConfig();
            plugin.reloadConfig();

            debug = getConfig().getBoolean("options.debug", false);
            bot_token = getConfig().getString("options.bot_token", "abc");
            chat_id = getConfig().getString("options.chat_id", "0");
            join_log_enabled = getConfig().getBoolean("options.join_log.enabled", true);
            join_log_silenced = getConfig().getBoolean("options.join_log.silenced", false);
            quit_log_enabled = getConfig().getBoolean("options.quit_log.enabled", true);
            quit_log_silenced = getConfig().getBoolean("options.quit_log.silenced", false);
            chat_log_enabled = getConfig().getBoolean("options.chat_log.enabled", true);
            chat_log_silenced = getConfig().getBoolean("options.chat_log.silenced", false);
            silenced_players = getConfig().getStringList("options.silenced_players");
            ignored_players = getConfig().getStringList("options.ignored_players");

            player_joined = getConfig().getString("messages.player_joined", "%player% joined the game");
            player_left = getConfig().getString("messages.player_left", "%player% left the game");
            player_chat = getConfig().getString("messages.player_chat", "%player% » %message%");
            configuration_reloaded = getConfig().getString("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            an_error_occurred = getConfig().getString("messages.an_error_occurred", "&cAn error occurred with this task..");
            unknown_subcommand = getConfig().getString("messages.unknown_subcommand", getConfig().getString("messages.unknown_subcommand", "&cUnknown subcommand"));
            no_permission = getConfig().getString("messages.no_permission", "&cYou're not permitted to do this.");
            help_admin = getConfig().getStringList("messages.help_admin");
            help_user = getConfig().getStringList("messages.help_user");

            plugin.getLogger().info("Configuration reloaded successfully");
            return true;
        } catch (Exception ex) {
            plugin.getLogger().severe("An exception occurred while trying to reload the whole configuration file");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    ///////////////////////////////

    public static void sendLog(String text, Boolean silenced) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                String text_ = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
                URL url;
                if (!silenced) {
                    url = new URL("https://api.telegram.org/bot" + Vars.bot_token + "/sendMessage?disable_web_page_preview=true&chat_id=" + Vars.chat_id + "&text=" + text_);
                } else {
                    url = new URL("https://api.telegram.org/bot" + Vars.bot_token + "/sendMessage?disable_web_page_preview=true&disable_notification=true&chat_id=" + Vars.chat_id + "&text=" + text_);
                }

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("content-type", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                connection.setRequestMethod("GET");
                connection.connect();

                String output = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();

                if (Vars.debug) {
                    plugin.getLogger().info(url.toString());
                    plugin.getLogger().info(output);
                }
            } catch (UnknownHostException ignored) {
            } catch (Exception ex) {
                plugin.getLogger().severe("An exception occurred while trying to send a log message on telegram");
                ex.printStackTrace();
            }
        });

    }

    ///////////////////////////////

    public static void sendString(String text, CommandSender sender) {
        if (plugin.getConfig().getString(text) != null) {
            text = plugin.getConfig().getString(text);
        }

        if (text.length() > 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', text.replaceAll("%version%", plugin.getDescription().getVersion()).replaceAll("%name%", sender.getName())));
        }
    }

    public static List<String> colorList(List<String> uncoloredList) {
        List<String> coloredList = new ArrayList<>();
        for(String line : uncoloredList) {
            coloredList.add(ChatColor.translateAlternateColorCodes('&', line.replaceAll("%version%", plugin.getDescription().getVersion())));
        }
        return coloredList;
    }

    public static Boolean containsIgnoreCase(List<String> list, String string) {
        for(String string_ : list) {
            if (string_.equalsIgnoreCase(string.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

}

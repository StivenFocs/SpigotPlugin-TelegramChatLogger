package cloud.stivenfocs.TelegramChatLogger.Events;

import cloud.stivenfocs.TelegramChatLogger.Loader;
import cloud.stivenfocs.TelegramChatLogger.Vars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class ChatEvents implements Listener {

    Loader plugin;
    public ChatEvents(Loader plugin) {
        this.plugin = plugin;
    }

    ///////////////////////////////

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent event) {
        if (Vars.join_log_enabled) {
            if (!Vars.containsIgnoreCase(Vars.ignored_players, event.getPlayer().getName())) {
                Boolean silenced = Vars.join_log_silenced;
                if (Vars.containsIgnoreCase(Vars.silenced_players, event.getPlayer().getName())) {
                    silenced = true;
                }

                Vars.sendLog(Vars.player_joined.replaceAll("%player%", event.getPlayer().getName()), silenced);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerQuit(PlayerQuitEvent event) {
        if (Vars.quit_log_enabled) {
            if (!Vars.containsIgnoreCase(Vars.ignored_players, event.getPlayer().getName())) {
                Boolean silenced = Vars.quit_log_silenced;
                if (Vars.containsIgnoreCase(Vars.silenced_players, event.getPlayer().getName())) {
                    silenced = true;
                }

                Vars.sendLog(Vars.player_left.replaceAll("%player%", event.getPlayer().getName()), silenced);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerChat(AsyncPlayerChatEvent event) {
        if (Vars.chat_log_enabled) {
            if (!Vars.containsIgnoreCase(Vars.ignored_players, event.getPlayer().getName())) {
                Boolean silenced = Vars.chat_log_silenced;
                if (Vars.containsIgnoreCase(Vars.silenced_players, event.getPlayer().getName())) {
                    silenced = true;
                }

                if (!event.isCancelled()) {
                    try {
                        Vars.sendLog(Vars.player_chat.replaceAll("%player%", event.getPlayer().getName()).replaceAll("%message%", Matcher.quoteReplacement(event.getMessage())), silenced);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

}

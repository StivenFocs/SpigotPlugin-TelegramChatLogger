package cloud.stivenfocs.TelegramChatLogger;

import cloud.stivenfocs.TelegramChatLogger.Commands.telegramchatlogger;
import cloud.stivenfocs.TelegramChatLogger.Events.ChatEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Loader extends JavaPlugin {

    @Override
    public void onEnable() {
        Vars.plugin = this;
        Vars vars = Vars.getVars();

        vars.reloadVars();

        getCommand("telegramchatlogger").setExecutor(new telegramchatlogger(this));
        getCommand("telegramchatlogger").setTabCompleter(new telegramchatlogger(this));
        Bukkit.getPluginManager().registerEvents(new ChatEvents(this), this);
    }

}

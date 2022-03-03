package cloud.stivenfocs.TelegramChatLogger.Commands;

import cloud.stivenfocs.TelegramChatLogger.Loader;
import cloud.stivenfocs.TelegramChatLogger.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class telegramchatlogger implements CommandExecutor, TabCompleter {

    Loader plugin;
    public telegramchatlogger(Loader plugin) {
        this.plugin = plugin;
    }

    ///////////////////////////////

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("tglogger.admin")) {
                sender.sendMessage(Vars.colorList(Vars.help_admin).toArray(new String[0]));
            } else {
                sender.sendMessage(Vars.colorList(Vars.help_user).toArray(new String[0]));
            }
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("tglogger.admin")) {
                    if (Vars.getVars().reloadVars()) {
                        Vars.sendString(Vars.configuration_reloaded, sender);
                    } else {
                        Vars.sendString(Vars.an_error_occurred, sender);
                    }
                } else {
                    Vars.sendString(Vars.no_permission, sender);
                }
                return true;
            }

            Vars.sendString(Vars.unknown_subcommand, sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        List<String> su = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("") || args[0].startsWith(" ")) {
                if (sender.hasPermission("tglogger.admin")) {
                    su.add("reload");
                }
            } else {
                if ("reload".startsWith(args[0].toLowerCase())) {
                    if (sender.hasPermission("tglogger.admin")) {
                        su.add("reload");
                    }
                }
            }
        }
        return su;
    }
}

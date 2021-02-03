package rarityeg.harmonyauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class CommandHandler implements CommandExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!LoginData.hasPlayerName(sender.getName())) {
            sender.sendMessage(ChatColor.GREEN + "你已经登录了！");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "必须输入密码！");
            return false;
        }
        String pwdConcat = String.join("<space>", args);
        if (ConfigReader.isPlayerRegistered(sender.getName())) {
            if (ConfigReader.verifyPassword(sender.getName(), pwdConcat)) {
                LoginData.removePlayerName(sender.getName());
                sender.sendMessage(ChatColor.GREEN + "登录成功，欢迎回来！");
            } else {
                sender.sendMessage(ChatColor.RED + "密码错误！");
            }
            return true;

        } else {
            ConfigReader.addPlayer(sender.getName(), pwdConcat);
            LoginData.removePlayerName(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "注册成功！");
            return true;
        }
    }
}

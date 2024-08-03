package turbo.bladeball.gameplay.util.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Component;
import turbo.bladeball.PlayerData;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;

@Component
public class WinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("myWin")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());
                WinRepositoryImpl winRepository = data.getWinRepository();
                player.sendMessage("Количество побед: " + winRepository.getWin());
            }
            return true;
        }
        return false;
    }
}

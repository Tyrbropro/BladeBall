package turbo.bladeball.gameplay.util.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Component;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.data.PlayerData;
import turbo.bladeball.register.SubCommand;

@Component
public class KillPlayerCommand  implements CommandExecutor {
    @Override
    @SubCommand("myKill")
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("myKill")) {
            if (commandSender instanceof Player player) {
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                KillRepositoryImpl killRepository = data.getKillRepository();
                player.sendMessage("Количество киллов: " + killRepository.getKill());
            }
            return true;
        }
        return false;
    }
}

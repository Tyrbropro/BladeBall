package turbo.bladeball.gameplay.util.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.PlayerData;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;

public class LoseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("myLose")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                LoseRepositoryImpl loseRepository = data.getLoseRepository();
                player.sendMessage("Количество поражений: " + loseRepository.getLose());
            }
            return true;
        }
        return false;
    }
}

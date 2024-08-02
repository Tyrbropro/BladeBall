package turbo.bladeball.gameplay.util.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.PlayerData;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;

public class MoneyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("money")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
                player.sendMessage("Количество денег: " + moneyRepository.getMoney());
            }
            return true;
        }
        return false;
    }
}

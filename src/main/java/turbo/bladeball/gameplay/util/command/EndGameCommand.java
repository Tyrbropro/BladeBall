package turbo.bladeball.gameplay.util.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.MapService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EndGameCommand implements CommandExecutor {

    BallConfig ballConfig;

    public EndGameCommand(BallConfig ballConfig) {
        this.ballConfig = ballConfig;
    }

    Location End = new Location(MapService.getWorld(), -190.5, 86, 272.5);

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("end")) {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                for (Player players : ballConfig.getPlayers()) {
                    if (player.equals(players)) {
                        ballConfig.getPlayers().remove(player);
                    }
                }
                player.teleport(End);

            }
            return true;
        }
        return false;
    }
}

package turbo.bladeball.gameplay.util.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.util.MapService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EndGameCommand implements CommandExecutor {

    BallConfig ballConfig;

    @Autowired
    public EndGameCommand(BallConfig ballConfig) {
        this.ballConfig = ballConfig;
    }

    Location end = new Location(MapService.getWorld(), -190.5, 86, 272.5);

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
                player.teleport(end);

            }
            return true;
        }
        return false;
    }
}

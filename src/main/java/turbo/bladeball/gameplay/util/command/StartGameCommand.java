package turbo.bladeball.gameplay.util.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.ball.MoveBall;
import turbo.bladeball.gameplay.util.MapService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StartGameCommand implements CommandExecutor {
    BallConfig ballConfig;
    MoveBall moveball;

    public StartGameCommand(BallConfig ballConfig , MoveBall moveball) {
        this.ballConfig = ballConfig;
        this.moveball = moveball;
    }
    Location Start = new Location(MapService.getWorld(), -203.5, 86, 272.5);

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("start")) {
            if (commandSender instanceof Player) {
                if (MapService.getWorld().getPlayers().size() >= 2) {
                    if (ballConfig.getPlayers().size() <= 1) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ballConfig.getPlayers().add(player);
                            player.teleport(Start);
                        }
                        moveball.move();
                    }
                }
            }
            return true;
        }
        return false;
    }
}
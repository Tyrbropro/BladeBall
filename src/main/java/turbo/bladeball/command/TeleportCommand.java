package turbo.bladeball.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.MapService;
import turbo.bladeball.gameplay.ball.MoveBall;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeleportCommand implements CommandExecutor {

    World world = MapService.getWorld();
    Location Start = new Location(world, -203.5, 86, 272.5);
    Location End = new Location(world, -190.5, 86, 272.5);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("start")) {
            if (commandSender instanceof Player) {
                if (MoveBall.getPlayers().size() <= 1) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        MoveBall.getPlayers().add(player);
                        player.teleport(Start);
                    }
                }
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("end")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                player.teleport(End);

            }
            return true;
        }
        return false;
    }
}

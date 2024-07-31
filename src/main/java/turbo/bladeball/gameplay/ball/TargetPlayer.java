package turbo.bladeball.gameplay.ball;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class TargetPlayer {

    public Player getNearestPlayerLookingAt(Player player, double maxDistance) {
        List<Entity> nearbyEntities = player.getNearbyEntities(maxDistance, maxDistance, maxDistance);

        Player nearestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player && !entity.equals(player)) {
                Player target = (Player) entity;
                Location targetLocation = target.getLocation();
                double distance = player.getEyeLocation().distance(targetLocation);

                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPlayer = target;
                }
            }
        }

        return nearestPlayer;
    }
}

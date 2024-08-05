package turbo.bladeball.gameplay.util.ballUtil;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.BallConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TargetPlayer {

    BallConfig ballConfig;

    @Autowired
    public TargetPlayer(BallConfig ballConfig){
        this.ballConfig = ballConfig;
    }

    public Player getNearestPlayerLookingAt(Player player, double maxDistance) {
        List<Entity> nearbyEntities = player.getNearbyEntities(maxDistance, maxDistance, maxDistance);

        Player nearestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player target && !entity.equals(player)) {
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

    public void setTarget(Player player) {
        ballConfig.setTarget(player);
        ballConfig.setNoTarget(null);

        ballConfig.setStart(ballConfig.getSlime().getLocation().toVector());
        ballConfig.setPlayerHitDirection(ballConfig.getTarget().getLocation().getDirection().normalize().multiply(-1));
        ballConfig.setCurrentPosition(ballConfig.getStart().clone());
        ballConfig.setInitialDirection(ballConfig.getPlayerHitDirection().clone());
    }

    public void changeTarget(Player player) {
        ballConfig.setNoTarget(player);
        ballConfig.setTarget(getNearestPlayerLookingAt(player, 100));

        ballConfig.setStart(ballConfig.getSlime().getLocation().toVector());
        ballConfig.setPlayerHitDirection(player.getLocation().getDirection().normalize());
        ballConfig.setCurrentPosition(ballConfig.getStart().clone());
        ballConfig.setInitialDirection(ballConfig.getPlayerHitDirection().clone());
    }

    public Player randomPlayer() {
        List<Player> playerList = new ArrayList<>(ballConfig.getPlayers());

        Random random = new Random();
        int randomIndex = random.nextInt(playerList.size());

        return playerList.get(randomIndex);
    }
}

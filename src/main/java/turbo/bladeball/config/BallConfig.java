package turbo.bladeball.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.util.MapService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Component
public class BallConfig {

    World world = MapService.getWorld();
    Location spawnLoc = new Location(world, -222, 87, 271);
    Slime slime = world.spawn(spawnLoc, Slime.class);

    HashMap<UUID, Integer> touchDistant = new HashMap<>();

    final Set<Player> players = new HashSet<>();

    Vector start;
    Vector playerHitDirection;
    Vector currentPosition;
    Vector initialDirection;

    Player target;
    Player noTarget;

    int hitDistant = 3;
    double speed;
    double tick;

    public void addDistance(UUID playerUUID, int distance) {
        touchDistant.put(playerUUID, distance);
    }
}

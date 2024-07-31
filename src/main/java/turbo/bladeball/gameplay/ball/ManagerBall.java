package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Slime;
import turbo.bladeball.MapService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManagerBall {
    static World world = MapService.getWorld();
    static Location spawnLoc = new Location(world, -222, 87, 271);

    @Getter
    static Slime slime = world.spawn(spawnLoc, Slime.class);

    public void spawnBall() {
        getSlime().setCustomName("Мяч");
        getSlime().setCustomNameVisible(true);
        getSlime().setSize(1);
        getSlime().setGravity(false);
        getSlime().setAI(false);
    }

    public void removeBall() {
        getSlime().remove();
    }
}

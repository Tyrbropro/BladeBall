package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class PlatformSkill extends Skill {

    public PlatformSkill() {
        super("Platform", 30 * 20);
    }

    @Override
    public void activate(Player player) {
        Location location = player.getLocation();
        Location platformStart = location.clone().add(-1, 4, -1);

        player.teleport(location.add(0, 5, 0));

        createPlatform(platformStart);
        new BukkitRunnable() {
            @Override
            public void run() {
                delPlatform(platformStart);
            }
        }.runTaskLater(BladeBall.getPlugin(), 200L);
    }

    private void createPlatform(Location platformStart) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                Location blockLocation = platformStart.clone().add(x, 0, z);
                blockLocation.getBlock().setType(Material.STONE);
            }
        }
    }

    private void delPlatform(Location platformStart) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                Location blockLocation = platformStart.clone().add(x, 0, z);
                blockLocation.getBlock().setType(Material.AIR);
            }
        }
    }
}
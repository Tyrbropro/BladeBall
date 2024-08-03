package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class PlatformSkill extends Skill {

    public PlatformSkill() {
        super("Platform", 30 * 20, 0);
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

    @Override
    public void activateEffect(Player player) {
        Location playerLocation = player.getLocation();
        Location circleCenter = playerLocation.clone().add(0, 5, 0);

        circleCenter.setX(Math.floor(circleCenter.getX()) + 0.5);
        circleCenter.setZ(Math.floor(circleCenter.getZ()) + 0.5);

        for (int i = 0; i < 150; i++) {
            double angle = 2 * Math.PI * i / 150;
            double x = circleCenter.getX() + 3.5 * Math.cos(angle);
            double z = circleCenter.getZ() + 3.5 * Math.sin(angle);
            Location particleLocation = new Location(player.getWorld(), x, circleCenter.getY(), z);
            player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, 0, 0, 0);
        }
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
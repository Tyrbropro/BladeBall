package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class WayPointSkill extends Skill {

    public WayPointSkill() {
        super("WayPoint", 30 * 20, 0);
    }

    @Override
    public void activate(Player player) {
        Location location = player.getLocation();
        player.sendMessage("Passive activate");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(location);
                player.sendMessage("Passive end");
            }
        }.runTaskLater(BladeBall.getPlugin(), 5 * 20);
    }

    private BukkitRunnable task;
    private int tick = 0;

    @Override
    public void activateEffect(Player player) {
        Location start = player.getLocation();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                tick++;
                if (tick == 5) {
                    task.cancel();
                }
                createCircleEffect(start);
            }
        };
        task.runTaskTimer(BladeBall.getPlugin(), 0, 20);
    }

    private void createCircleEffect(Location location) {
        for (int i = 0; i < 30; i++) {
            double angle = 2 * Math.PI * i / 30;
            double x = location.getX() + 1 * Math.cos(angle);
            double z = location.getZ() + 1 * Math.sin(angle);
            Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);
            location.getWorld().spawnParticle(Particle.DRAGON_BREATH, particleLocation, 0, 0, 0, 0);
        }
    }
}

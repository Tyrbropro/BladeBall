package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class WindCloakSkill extends Skill {

    public WindCloakSkill() {
        super("WindCloak", 20 * 20, 0);
    }

    @Override
    public void activate(Player player) {
        player.setWalkSpeed(0.5F);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setWalkSpeed(0.2F);
            }
        }.runTaskLater(BladeBall.getPlugin(), 80L);
    }

    private BukkitRunnable task;
    private int tick = 0;

    @Override
    public void activateEffect(Player player) {
        World world = player.getWorld();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                tick++;
                Location particleLoc = player.getLocation();
                world.spawnParticle(Particle.VILLAGER_HAPPY, particleLoc, 1, 0, 0, 0, 0);
                if (tick >= 80) {
                    tick -= 80;
                    task.cancel();
                }
            }
        };
        task.runTaskTimer(BladeBall.getPlugin(), 0, 1);
    }
}
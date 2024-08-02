package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class WayPointSkill extends Skill {
    public WayPointSkill() {
        super("WayPoint", 30 * 20);
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
        }.runTaskLater(BladeBall.getPlugin(BladeBall.class), 100);
    }
}

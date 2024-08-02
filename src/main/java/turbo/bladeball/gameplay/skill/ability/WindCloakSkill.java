package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.gameplay.skill.Skill;

public class WindCloakSkill extends Skill {

    public WindCloakSkill() {
        super("WindCloak", 20 * 20);
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
}
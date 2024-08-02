package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class SwapSkill extends Skill {

    public SwapSkill(TargetPlayer targetPlayer) {
        super("Swap", 50 * 20 , targetPlayer);
    }

    @Override
    public void activate(Player player) {
        Player target = targetPlayer.randomPlayer();
        while (player.equals(target)) target = targetPlayer.randomPlayer();

        Location playerLoc = player.getLocation();
        Location targetLoc = target.getLocation();

        player.teleport(targetLoc);
        target.teleport(playerLoc);

    }
}

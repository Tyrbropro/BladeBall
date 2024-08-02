package turbo.bladeball.gameplay.skill.ability;

import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class PullSkill extends Skill {

    public PullSkill(TargetPlayer targetPlayer) {
        super("Pull", 30 * 20 , targetPlayer);
    }

    @Override
    public void activate(Player player) {
        targetPlayer.setTarget(player);
    }
}

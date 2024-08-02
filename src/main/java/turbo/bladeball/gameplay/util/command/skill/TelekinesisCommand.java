package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.TelekinesisSkill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class TelekinesisCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;
    BallConfig ballConfig;

    public TelekinesisCommand(SkillListener skillListener, TargetPlayer targetPlayer,BallConfig ballConfig){
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
        this.ballConfig = ballConfig;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("telekinesis")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new TelekinesisSkill(targetPlayer,ballConfig);
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}

package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.SwapSkill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

public class SwapCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;

    public SwapCommand(SkillListener skillListener, TargetPlayer targetPlayer){
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("swap")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new SwapSkill(targetPlayer);
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}

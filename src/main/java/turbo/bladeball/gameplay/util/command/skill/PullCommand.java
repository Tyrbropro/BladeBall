package turbo.bladeball.gameplay.util.command.skill;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.PullSkill;
import turbo.bladeball.gameplay.util.ballUtil.TargetPlayer;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PullCommand implements CommandExecutor {

    SkillListener skillManager;
    TargetPlayer targetPlayer;

    public PullCommand(SkillListener skillListener,TargetPlayer targetPlayer){
        this.skillManager = skillListener;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("pull")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new PullSkill(targetPlayer);
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}

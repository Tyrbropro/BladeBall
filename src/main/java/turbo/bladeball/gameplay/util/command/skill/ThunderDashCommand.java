package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.ThunderDashSkill;

public class ThunderDashCommand implements CommandExecutor {

    SkillListener skillManager;

    public ThunderDashCommand(SkillListener skillListener){
        this.skillManager = skillListener;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("thunderDash")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new ThunderDashSkill();
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}

package turbo.bladeball.gameplay.util.command.skill;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillListener;
import turbo.bladeball.gameplay.skill.ability.WayPointSkill;

public class WayPointCommand implements CommandExecutor {

    SkillListener skillManager;

    public WayPointCommand(SkillListener skillListener){
        this.skillManager = skillListener;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("wayPoint")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Skill skill = new WayPointSkill();
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}
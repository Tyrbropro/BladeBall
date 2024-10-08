package turbo.bladeball.gameplay.skill;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.SkillConfig;

import java.util.*;

@Component
@FieldDefaults (level = AccessLevel.PRIVATE,makeFinal = true)
public class SkillListener {

    SkillConfig skillConfig;

    @Autowired
    public SkillListener(SkillConfig skillConfig){
        this.skillConfig = skillConfig;
    }
    public void unlockSkill(UUID uuid, Skill skill) {
        skillConfig.getPlayerUnlockedSkills().computeIfAbsent(uuid, k -> new HashSet<>()).add(skill);
    }

    public void equipSkill(UUID uuid, Skill skill) {
        if (skillConfig.getPlayerUnlockedSkills().getOrDefault(uuid, new HashSet<>()).contains(skill)) {
            skillConfig.getPlayerEquippedSkills().put(uuid, skill);
        }
    }
    public Skill getEquippedSkill(UUID uuid) {
        return skillConfig.getPlayerEquippedSkills().get(uuid);
    }
}

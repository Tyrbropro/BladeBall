package turbo.bladeball.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import turbo.bladeball.gameplay.skill.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@Getter
public class SkillConfig {
    Map<UUID, Set<Skill>> playerUnlockedSkills = new HashMap<>();
    Map<UUID, Skill> playerEquippedSkills = new HashMap<>();
}

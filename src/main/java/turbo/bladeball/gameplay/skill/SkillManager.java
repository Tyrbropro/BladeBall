package turbo.bladeball.gameplay.skill;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults (level = AccessLevel.PRIVATE , makeFinal = true)
public class SkillManager {
    static Map<UUID, Set<Skill>> playerUnlockedSkills = new HashMap<>();
    static Map<UUID, Skill> playerEquippedSkills = new HashMap<>();


    public void unlockSkill(UUID uuid, Skill skill) {
        playerUnlockedSkills.computeIfAbsent(uuid, k -> new HashSet<>()).add(skill);
    }

    public void equipSkill(UUID uuid, Skill skill) {
        if (playerUnlockedSkills.getOrDefault(uuid, new HashSet<>()).contains(skill)) {
            playerEquippedSkills.put(uuid, skill);
        }
    }
    public Skill getEquippedSkill(UUID uuid) {
        return playerEquippedSkills.get(uuid);
    }
}

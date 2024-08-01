package turbo.bladeball.gameplay.skill;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@RequiredArgsConstructor
public abstract class Skill {
    private final String name;
    private final int cooldown;

    public abstract void activate(Player player);

    public boolean canUse(Player player) {
        return !player.hasPotionEffect(PotionEffectType.LUCK);
    }

    public void use(Player player) {
        if (canUse(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1));
            activate(player);
        } else player.sendMessage("У вас cooldown");
    }
}

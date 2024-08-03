package turbo.bladeball.gameplay.skill.ability;

import de.slikey.effectlib.effect.ExplodeEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.gameplay.skill.Skill;

import java.util.UUID;

public class TitanBladeSkill extends Skill {

    public TitanBladeSkill(BallConfig ballConfig) {
        super("TitanBlade", 50 * 20, 0.3f, ballConfig);
    }

    @Override
    public void activate(Player player) {
        UUID uuid = player.getUniqueId();

        ballConfig.addDistance(uuid, 6);
        givePlayerGoldSword(player);

        player.sendMessage("Passive activate");

        new BukkitRunnable() {
            @Override
            public void run() {
                ballConfig.addDistance(uuid, 3);
                removePlayerGoldSword(player);

                player.sendMessage("Passive end");
            }
        }.runTaskLater(BladeBall.getPlugin(), 200);
    }

    @Override
    public void activateEffect(Player player) {
        Location location = player.getLocation().add(0, 1, 0);

        ExplodeEffect explodeEffect = new ExplodeEffect(em);
        explodeEffect.setLocation(location);
        explodeEffect.start();
    }

    private void givePlayerGoldSword(Player player) {
        ItemStack goldSword = new ItemStack(Material.GOLD_SWORD);
        player.getInventory().setItemInMainHand(goldSword);
    }

    private void removePlayerGoldSword(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand != null && itemInHand.getType() == Material.GOLD_SWORD) {
            player.getInventory().setItemInMainHand(null);
        }
    }
}

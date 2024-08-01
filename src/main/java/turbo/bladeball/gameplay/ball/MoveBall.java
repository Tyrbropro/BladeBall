package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import turbo.bladeball.BladeBall;
import turbo.bladeball.PlayerData;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;

import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoveBall {

    @Getter
    static final Set<Player> players = new HashSet<>();

    @Getter
    static Player target;
    static Player noTarget;

    static double speed;
    static double tick;

    static Vector start;
    static Vector playerHitDirection;
    static Vector currentPosition;
    static Vector initialDirection;

    public void move() {
        Slime slime = ManagerBall.getSlime();
        target = randomPlayer();

        if (checkTarget()) return;
        initializeBukkitRunnable(slime);
    }

    public void changeTarget(Player player) {
        noTarget = player;
        target = getNearestPlayerLookingAt(player);

        start = ManagerBall.getSlime().getLocation().toVector();
        playerHitDirection = player.getLocation().getDirection().normalize();
        currentPosition = start.clone();
        initialDirection = playerHitDirection.clone();
    }

    public void setTarget(Player player) {
        target = player;

        start = ManagerBall.getSlime().getLocation().toVector();
        playerHitDirection = target.getLocation().getDirection().normalize().multiply(-1);
        currentPosition = start.clone();
        initialDirection = playerHitDirection.clone();
    }

    private void glowing() {
        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
        if (noTarget != null && noTarget.hasPotionEffect(PotionEffectType.GLOWING)) {
            noTarget.removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    private Player randomPlayer() {
        List<Player> playerList = new ArrayList<>(getPlayers());

        Random random = new Random();
        int randomIndex = random.nextInt(playerList.size());

        return playerList.get(randomIndex);
    }

    private boolean checkTarget() {
        if (target == null) return true;
        for (Player player : getPlayers()) {
            if (player.equals(target)) {
                return false;
            }
        }
        return true;
    }

    private Player getNearestPlayerLookingAt(Player player) {
        TargetPlayer targetPlayer = new TargetPlayer();
        return targetPlayer.getNearestPlayerLookingAt(player, 100);
    }

    private void initializeBukkitRunnable(Slime slime) {
        start = slime.getLocation().toVector();
        playerHitDirection = start;
        if (noTarget != null) playerHitDirection = noTarget.getLocation().getDirection().normalize();

        speed = 2.0;
        tick = 0;

        currentPosition = start.clone();
        initialDirection = playerHitDirection.clone();
        new BukkitRunnable() {
            @Override
            public void run() {
                moveSlimeTowardsTarget(this, slime, start, currentPosition, initialDirection);
            }
        }.runTaskTimer(BladeBall.getPlugin(BladeBall.class), 0L, 1L);
    }

    private void moveSlimeTowardsTarget(BukkitRunnable runnable, Slime slime, Vector start, Vector currentPosition, Vector initialDirection) {
        if (checkTarget()) {
            runnable.cancel();
            endInteraction(slime);
            return;
        }
        glowing();
        Vector end = target.getLocation().toVector();

        if (++tick >= 20) {
            speed *= 1.05;
            tick -= 20;
        }

        double t = calculateInterpolationFactor(currentPosition, end, start);

        Vector toEnd = end.clone().subtract(currentPosition).normalize();
        Vector currentDirection = toEnd.multiply(t).add(initialDirection.multiply(1 - t)).normalize();

        currentPosition.add(currentDirection.multiply(0.1 * speed));
        if (currentPosition.getY() < 87) {
            currentPosition.setY(87);
        }
        slime.teleport(new Location(slime.getWorld(), currentPosition.getX(), currentPosition.getY(), currentPosition.getZ()));

        Location targetLoc = target.getLocation();
        if (targetLoc.distance(slime.getLocation()) <= 1.01) {
            endInteraction(slime);
            runnable.cancel();
        }
    }

    private double calculateInterpolationFactor(Vector currentPosition, Vector end, Vector start) {
        double distanceToEnd = currentPosition.distance(end);
        double totalDistance = start.distance(end);

        double t = 1.2 - (distanceToEnd / totalDistance);
        if (t < 0.2) {
            t = 0.2;
        } else if (t >= 1) {
            t = 0.8;
        }
        return t;
    }

    private void endInteraction(Slime slime) {
        if (noTarget != null) {
            giveKillReward();
        }
        giveLoseReward();
        if (getPlayers().size() == 1) {
            List<Player> playerList = new ArrayList<>(getPlayers());

            playerList.get(0).teleport(new Location(playerList.get(0).getWorld(), -190.5, 86, 272.5));
            giveWinReward(playerList.get(0));
        }
        slime.teleport(new Location(slime.getWorld(), -222, 87, 271));
    }

    private void giveKillReward() {
        PlayerData data = PlayerData.getUsers().get(noTarget.getUniqueId());
        MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
        KillRepositoryImpl killRepository = data.getKillRepository();

        moneyRepository.setMoney(moneyRepository.getMoney() + 10);
        killRepository.setKill(killRepository.getKill() + 1);
        noTarget.sendMessage("+10 Monet za kill");
    }

    private void giveWinReward(Player player) {
        PlayerData data = PlayerData.getUsers().get(player.getUniqueId());
        MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
        WinRepositoryImpl winRepository = data.getWinRepository();

        winRepository.setWin(winRepository.getWin() + 1);
        moneyRepository.setMoney(moneyRepository.getMoney() + 30);
        player.sendMessage("+30 Monet za pobedy");
    }

    private void giveLoseReward() {
        if (target == null) return;

        PlayerData data = PlayerData.getUsers().get(target.getUniqueId());
        LoseRepositoryImpl loseRepository = data.getLoseRepository();
        loseRepository.setLose(loseRepository.getLose() + 1);

        getPlayers().remove(target);
        target.sendMessage("Вы проиграли");
        target.setHealth(0.0);
        target = null;
    }

}
package turbo.bladeball.gameplay.ball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import turbo.bladeball.BladeBall;
import turbo.bladeball.config.BallConfig;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;
import turbo.bladeball.data.PlayerData;
import turbo.bladeball.gameplay.util.MapService;
import turbo.bladeball.gameplay.util.ball.TargetPlayer;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MoveBall {

    BallConfig ballConfig;
    TargetPlayer targetPlayer;

    @Autowired
    public MoveBall(BallConfig ballConfig, TargetPlayer targetPlayer) {
        this.ballConfig = ballConfig;
        this.targetPlayer = targetPlayer;
    }

    public void move() {
        Slime slime = ballConfig.getSlime();
        ballConfig.setTarget(targetPlayer.randomPlayer());

        if (checkTarget()) return;
        initializeBukkitRunnable(slime);
    }

    private void glowing() {
        ballConfig.getTarget().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 6000, 1));

        var noTarget = ballConfig.getNoTarget();
        if (noTarget != null && noTarget.hasPotionEffect(PotionEffectType.GLOWING)) {
            noTarget.removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    private boolean checkTarget() {
        var target = ballConfig.getTarget();

        if (target == null) return true;

        for (Player player : ballConfig.getPlayers()) {
            if (player.equals(target)) {
                return false;
            }
        }
        return true;
    }

    private void initializeBukkitRunnable(Slime slime) {
        var noTarget = ballConfig.getNoTarget();

        ballConfig.setStart(ballConfig.getSlime().getLocation().toVector());
        var start = ballConfig.getStart();

        ballConfig.setPlayerHitDirection(start);
        if (noTarget != null)
            ballConfig.setPlayerHitDirection(noTarget.getLocation().getDirection().normalize());

        ballConfig.setSpeed(2);
        ballConfig.setTick(0);

        ballConfig.setCurrentPosition(start.clone());
        ballConfig.setInitialDirection(ballConfig.getPlayerHitDirection().clone());
        new BukkitRunnable() {
            @Override
            public void run() {
                moveSlimeTowardsTarget(this, slime, start, ballConfig.getCurrentPosition(), ballConfig.getInitialDirection());
            }
        }.runTaskTimer(BladeBall.getPlugin(), 0L, 1L);
    }

    private void moveSlimeTowardsTarget(BukkitRunnable runnable, Slime slime, Vector start, Vector currentPosition, Vector initialDirection) {
        if (checkTarget()) {
            runnable.cancel();
            endInteraction(slime);
            return;
        }
        glowing();
        Vector end = ballConfig.getTarget().getLocation().toVector();

        ballConfig.setTick(ballConfig.getTick() + 1);
        if (ballConfig.getTick() >= 20) {
            ballConfig.setSpeed(ballConfig.getSpeed() * 1.05);
            ballConfig.setTick(ballConfig.getTick() - 20);
        }

        double t = calculateInterpolationFactor(currentPosition, end, start);

        Vector toEnd = end.clone().subtract(currentPosition).normalize();
        Vector currentDirection = toEnd.multiply(t).add(initialDirection.multiply(1 - t)).normalize();

        currentPosition.add(currentDirection.multiply(0.1 * ballConfig.getSpeed()));
        if (currentPosition.getY() < 87) {
            currentPosition.setY(87);
        }
        Location location1 = slime.getLocation();

        slime.teleport(new Location(slime.getWorld(), currentPosition.getX(), currentPosition.getY(), currentPosition.getZ()));

        Location location2 = slime.getLocation();

        Location targetLoc = ballConfig.getTarget().getLocation();
        if (targetLoc.distance(slime.getLocation()) <= 1.02) {
            endInteraction(slime);
            runnable.cancel();
        } else if (isPlayerOnLine(targetLoc, location1, location2)) {
            endInteraction(slime);
            runnable.cancel();
        }
    }

    private boolean isPlayerOnLine(Location playerLocation, Location location1, Location location2) {
        Vector A = location1.toVector();
        Vector B = location2.toVector();
        Vector P = playerLocation.toVector();

        Vector AB = B.clone().subtract(A);
        Vector AP = P.clone().subtract(A);
        Vector BP = P.clone().subtract(B);

        double crossProductLengthSquared = AB.crossProduct(AP).lengthSquared();
        if (crossProductLengthSquared > 1e-6) {
            return false;
        }

        double dotProduct1 = AB.dot(AP);
        double dotProduct2 = -AB.dot(BP);
        return dotProduct1 >= 0 && dotProduct2 >= 0;
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
        if (ballConfig.getNoTarget() != null) {
            giveKillReward();
        }
        giveLoseReward();
        if (ballConfig.getPlayers().size() == 1) {
            List<Player> playerList = new ArrayList<>(ballConfig.getPlayers());

            playerList.get(0).teleport(new Location(playerList.get(0).getWorld(), -190.5, 86, 272.5));
            clearEffects(playerList.get(0));
            giveWinReward(playerList.get(0));
            ballConfig.setNoTarget(null);
        }
        slime.teleport(new Location(slime.getWorld(), -222, 87, 271));
    }

    private void giveKillReward() {
        var noTarget = ballConfig.getNoTarget();

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
        var target = ballConfig.getTarget();
        if (target == null) return;

        PlayerData data = PlayerData.getUsers().get(target.getUniqueId());
        LoseRepositoryImpl loseRepository = data.getLoseRepository();
        loseRepository.setLose(loseRepository.getLose() + 1);

        ballConfig.getPlayers().remove(target);
        target.sendMessage("Вы проиграли");
        target.teleport(new Location(MapService.getWorld(), -190.5, 86, 272.5));
        clearEffects(target);

        ballConfig.setTarget(null);
    }

    private void clearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
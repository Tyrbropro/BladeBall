package turbo.bladeball.gameplay.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import turbo.bladeball.PlayerData;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;
import turbo.bladeball.gameplay.ball.MoveBall;
import turbo.bladeball.gameplay.skill.PullSkill;
import turbo.bladeball.gameplay.skill.Skill;
import turbo.bladeball.gameplay.skill.SkillManager;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Command implements CommandExecutor {

    World world = MapService.getWorld();
    Location Start = new Location(world, -203.5, 86, 272.5);
    Location End = new Location(world, -190.5, 86, 272.5);
    MoveBall moveBall = new MoveBall();
    SkillManager skillManager = new SkillManager();

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("start")) {
            if (commandSender instanceof Player) {
                if (world.getPlayers().size() >= 2) {
                    if (MoveBall.getPlayers().size() <= 1) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            MoveBall.getPlayers().add(player);
                            player.teleport(Start);
                        }
                        moveBall.move();
                    }
                }
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("end")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                for (Player players : MoveBall.getPlayers()) {
                    if (player.equals(players)) {
                        MoveBall.getPlayers().remove(player);
                    }
                }
                player.teleport(End);

            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("money")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                MoneyRepositoryImpl moneyRepository = data.getMoneyRepository();
                player.sendMessage("Количество денег: " + moneyRepository.getMoney());
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("myKill")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                KillRepositoryImpl killRepository = data.getKillRepository();
                player.sendMessage("Количество киллов: " + killRepository.getKill());
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("myWin")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                WinRepositoryImpl winRepository = data.getWinRepository();
                player.sendMessage("Количество побед: " + winRepository.getWin());
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("myLose")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                PlayerData data = PlayerData.getUsers().get(player.getUniqueId());

                LoseRepositoryImpl loseRepository = data.getLoseRepository();
                player.sendMessage("Количество поражений: " + loseRepository.getLose());
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("pull")) {

            if (commandSender instanceof Player) {

                Player player = (Player) commandSender;
                Skill skill = new PullSkill();
                skillManager.unlockSkill(player.getUniqueId(),skill);
                skillManager.equipSkill(player.getUniqueId(),skill);
            }
            return true;
        }
        return false;
    }
}

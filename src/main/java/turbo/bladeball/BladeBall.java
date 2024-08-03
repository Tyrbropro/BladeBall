package turbo.bladeball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import turbo.bladeball.config.Config;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.util.MapService;
import turbo.bladeball.gameplay.util.command.*;
import turbo.bladeball.gameplay.util.command.skill.*;
import turbo.bladeball.gameplay.util.event.Event;

import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BladeBall extends JavaPlugin {

    private static BladeBall plugin;

    ConfigurableApplicationContext context;
    BallListener ballListener;

    @Override
    public void onEnable() {
        plugin = this;

        World world = MapService.getWorld();
        if (world != null) {
            context = new AnnotationConfigApplicationContext(Config.class);

            ballListener = context.getBean(BallListener.class);

            registerEvents();
            registerCommands();

            ballListener.spawnBall();
        } else Bukkit.shutdown();

    }

    @Override
    public void onDisable() {
        ballListener.removeBall();

        if (context != null) {
            context.close();
        }
    }

    public static BladeBall getPlugin() {
        return plugin;
    }

    private void registerCommands() {
        Map<String, CommandExecutor> commands = new HashMap<>();

        commands.put("start", context.getBean(StartGameCommand.class));
        commands.put("end", context.getBean(EndGameCommand.class));
        commands.put("money", context.getBean(MoneyCommand.class));
        commands.put("myKill", context.getBean(KillPlayerCommand.class));
        commands.put("myWin", context.getBean(WinCommand.class));
        commands.put("myLose", context.getBean(LoseCommand.class));
        commands.put("pull", context.getBean(PullCommand.class));
        commands.put("platform", context.getBean(PlatformCommand.class));
        commands.put("dash", context.getBean(DashCommand.class));
        commands.put("superJump", context.getBean(SuperJumpCommand.class));
        commands.put("swap", context.getBean(SwapCommand.class));
        commands.put("windCloak", context.getBean(WindCloakCommand.class));
        commands.put("wayPoint", context.getBean(WayPointCommand.class));
        commands.put("titanBlade", context.getBean(TitanBladeCommand.class));
        commands.put("thunderDash", context.getBean(ThunderDashCommand.class));
        commands.put("telekinesis", context.getBean(TelekinesisCommand.class));

        for (Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
            getCommand(entry.getKey()).setExecutor(entry.getValue());
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new MapService(), this);
        getServer().getPluginManager().registerEvents(context.getBean(Event.class), this);
    }
}

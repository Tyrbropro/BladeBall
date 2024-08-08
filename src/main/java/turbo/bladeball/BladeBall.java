package turbo.bladeball;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import turbo.bladeball.config.Config;
import turbo.bladeball.data.DataBase;
import turbo.bladeball.data.PlayerData;
import turbo.bladeball.gameplay.ball.BallListener;
import turbo.bladeball.gameplay.util.MapService;
import turbo.bladeball.register.CommandService;
import turbo.bladeball.register.CommandServiceImpl;
import turbo.bladeball.register.ListenerService;
import turbo.bladeball.register.ListenerServiceImpl;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BladeBall extends JavaPlugin {

    static BladeBall plugin;

    static ConfigurableApplicationContext context;
    BallListener ballListener;

    @Override
    public void onEnable() {
        plugin = this;

        World world = MapService.getWorld();
        if (world != null) {
            context = new AnnotationConfigApplicationContext(Config.class);

            DataBase.loadDatabaseConfig();

            ballListener = context.getBean(BallListener.class);

            ListenerService listenerService = context.getBean(ListenerServiceImpl.class);
            listenerService.scanPackages(this.getClass().getPackage().getName());

            CommandService commandService = context.getBean(CommandServiceImpl.class);
            commandService.scanPackage(this.getClass().getPackage().getName());

            ballListener.spawnBall();
        } else Bukkit.shutdown();

    }

    @Override
    public void onDisable() {

        for (PlayerData playerData : PlayerData.getUsers().values()) {
            playerData.saveToMongoDB();
        }

        ballListener.removeBall();

        if (context != null) {
            context.close();
        }
    }

    public static BladeBall getPlugin() {
        return plugin;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}

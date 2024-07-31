package turbo.bladeball;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import turbo.bladeball.gameplay.ball.ManagerBall;
import turbo.bladeball.gameplay.utils.Command;
import turbo.bladeball.gameplay.utils.MapService;
import turbo.bladeball.gameplay.utils.event.Event;

public final class BladeBall extends JavaPlugin {

    @Override
    public void onEnable() {

        World world = MapService.getWorld();

        if (world != null) {

            getServer().getPluginManager().registerEvents(new MapService(), this);
            getServer().getPluginManager().registerEvents(new Event(), this);

            getCommand("start").setExecutor(new Command());
            getCommand("end").setExecutor(new Command());
            getCommand("money").setExecutor(new Command());
            getCommand("myKill").setExecutor(new Command());
            getCommand("myWin").setExecutor(new Command());

            ManagerBall managerBall = new ManagerBall();
            managerBall.spawnBall();

        } else Bukkit.shutdown();
    }

    @Override
    public void onDisable() {
        ManagerBall managerBall = new ManagerBall();
        managerBall.removeBall();
    }
}

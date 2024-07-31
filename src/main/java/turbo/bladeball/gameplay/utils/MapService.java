package turbo.bladeball.gameplay.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class MapService implements Listener {

    @Getter
    private static World world = Bukkit.getWorld("world");

    @EventHandler
    public void onWorldLoad(WorldInitEvent event) {
        world = Bukkit.getWorld("world");
        System.out.println("Мир прогрузился");
    }
}

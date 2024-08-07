package turbo.bladeball.register;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import turbo.bladeball.BladeBall;

@Service
public class ListenerServiceImpl implements ListenerService {

    @Override
    public void scanPackages(String packageName) {
        new Reflections(packageName)
                .getSubTypesOf(Listener.class)
                .forEach(clazz -> {
                    try {
                        Listener listener = BladeBall.getContext().getBean(clazz);
                        this.registerListener(listener);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, BladeBall.getPlugin());
    }
}

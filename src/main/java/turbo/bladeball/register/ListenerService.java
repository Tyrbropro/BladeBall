package turbo.bladeball.register;

import org.bukkit.event.Listener;

public interface ListenerService {

    void registerListener(Listener listener);

    void scanPackages(String packageName);
}

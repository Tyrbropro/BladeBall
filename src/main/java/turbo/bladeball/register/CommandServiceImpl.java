package turbo.bladeball.register;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import turbo.bladeball.BladeBall;

import java.lang.reflect.Method;

@Service
public class CommandServiceImpl implements CommandService {

    @Override
    public void scanPackage(String packageName) {
        new Reflections(packageName)
                .getSubTypesOf(CommandExecutor.class)
                .stream()
                .filter(clazz -> clazz.getPackage().getName().startsWith(packageName))
                .forEach(clazz -> {
                    try {
                        if (clazz.equals(BladeBall.class)) return;
                        CommandExecutor command = BladeBall.getContext().getBean(clazz);
                        this.registerCommand(command);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void registerCommand(CommandExecutor command) {
        for (Method method : command.getClass().getMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                String commandName = method.getAnnotation(SubCommand.class).value();
                Bukkit.getServer().getPluginCommand(commandName).setExecutor(command);
            }
        }
    }
}
package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public abstract class OneOptionalArgumentCommand<T> implements TabExecutor {

    protected abstract T parse(Player player);

    protected abstract T parse(String argument) throws CommandException;

    protected abstract void execute(CommandSender sender, T value);

    protected abstract Stream<String> suggest(CommandSender sender);

    protected abstract @Nullable String getArgumentPermission(CommandSender sender, T argument);

    protected boolean isDenied(CommandSender sender, T argument) {
        return false;
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && args.length < 1)
            return false;

        T value;

        if (args.length == 0) {
            value = parse((Player) sender);
        } else if (args.length > 1) {
            return false;
        } else {
            value = parse(args[0]);
        }
        var permission = getArgumentPermission(sender, value);
        if (permission == null || sender.hasPermission(permission))
            execute(sender, value);
        else throw new NoPermissionException(permission);
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length > 1 ? null : suggest(sender).toList();
    }
}

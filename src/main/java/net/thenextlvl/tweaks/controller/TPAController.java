package net.thenextlvl.tweaks.controller;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

@NullMarked
public final class TPAController {
    private final Map<Player, Set<Request>> requests = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public TPAController(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void removeRequests(final Player player) {
        requests.values().forEach(requests -> requests.removeIf(request -> request.player().equals(player)));
        requests.values().removeIf(Collection::isEmpty);
        requests.remove(player);
    }

    public Optional<Request> getRequest(final Player player, final Player target) {
        return getRequests(player).stream()
                .filter(request -> request.player().equals(target))
                .findAny();
    }

    public List<Request> getRequests(final Player player) {
        final var requests = this.requests.get(player);
        if (requests == null) return List.of();
        return List.copyOf(requests);
    }

    public boolean addRequest(final Player player, final Player sender, final RequestType type) {
        final var players = requests.computeIfAbsent(player, ignored -> new HashSet<>());
        return players.stream().noneMatch(request -> request.player().equals(sender))
               && players.add(new Request(sender, type));
    }

    public void expireRequest(final Player player, final Player sender, final RequestType type) {
        if (!removeRequest(player, sender, type)) return;
        plugin.bundle().sendMessage(sender, "command.tpa.timeout.self",
                Placeholder.parsed("player", player.getName()));
        plugin.bundle().sendMessage(player, "command.tpa.timeout",
                Placeholder.parsed("player", sender.getName()));
    }

    public boolean removeRequest(final Player sender, final Player player, final RequestType type) {
        final var players = requests.get(sender);
        final var result = players != null && players.removeIf(request ->
                request.player().equals(player) && request.type() == type);
        if (players != null && players.isEmpty()) requests.remove(sender);
        return result;
    }

    public record Request(Player player, RequestType type) {
    }

    public enum RequestType {
        TPA_HERE("command.tpa.here.incoming", "command.tpa.here.outgoing"),
        TPA("command.tpa.incoming", "command.tpa.outgoing");

        private final String incomingMessage;
        private final String outgoingMessage;

        RequestType(final String incomingMessage, final String outgoingMessage) {
            this.incomingMessage = incomingMessage;
            this.outgoingMessage = outgoingMessage;
        }

        public String incomingMessage() {
            return incomingMessage;
        }

        public String outgoingMessage() {
            return outgoingMessage;
        }
    }
}

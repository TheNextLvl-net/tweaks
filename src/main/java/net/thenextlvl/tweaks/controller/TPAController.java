package net.thenextlvl.tweaks.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class TPAController {
    private final Map<Player, Set<Request>> requests = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public void removeRequests(Player player) {
        requests.values().forEach(requests -> requests.removeIf(request -> request.player().equals(player)));
        requests.values().removeIf(Collection::isEmpty);
        requests.remove(player);
    }

    public Optional<Request> getRequest(Player player, Player target) {
        return getRequests(player).stream()
                .filter(request -> request.player().equals(target))
                .findAny();
    }

    public List<Request> getRequests(Player player) {
        var requests = this.requests.get(player);
        if (requests == null) return List.of();
        return List.copyOf(requests);
    }

    public boolean addRequest(Player player, Player sender, RequestType type) {
        var players = requests.computeIfAbsent(player, ignored -> new HashSet<>());
        return players.stream().noneMatch(request -> request.player().equals(sender))
               && players.add(new Request(sender, type));
    }

    public void expireRequest(Player player, Player sender, RequestType type) {
        if (!removeRequest(player, sender, type)) return;
        plugin.bundle().sendMessage(sender, "command.tpa.timeout.self",
                Placeholder.parsed("player", player.getName()));
        plugin.bundle().sendMessage(player, "command.tpa.timeout",
                Placeholder.parsed("player", sender.getName()));
    }

    public boolean removeRequest(Player sender, Player player, RequestType type) {
        var players = requests.get(sender);
        var result = players != null && players.removeIf(request ->
                request.player().equals(player) && request.type() == type);
        if (players != null && players.isEmpty()) requests.remove(sender);
        return result;
    }

    public record Request(Player player, RequestType type) {
    }

    @Getter
    @RequiredArgsConstructor
    @Accessors(fluent = true)
    public enum RequestType {
        TPA_HERE("command.tpa.here.incoming", "command.tpa.here.outgoing"),
        TPA("command.tpa.incoming", "command.tpa.outgoing");

        private final String incomingMessage;
        private final String outgoingMessage;
    }
}
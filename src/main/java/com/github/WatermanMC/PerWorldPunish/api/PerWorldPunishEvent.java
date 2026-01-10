package com.github.WatermanMC.PerWorldPunish.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public abstract class PerWorldPunishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final UUID playerId;
    private final String worldName;
    private final String reason;

    public PerWorldPunishEvent(UUID playerId, String worldName, String reason) {
        this.playerId = playerId;
        this.worldName = worldName;
        this.reason = reason;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(playerId);
    }

    public String getWorldName() {
        return worldName;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
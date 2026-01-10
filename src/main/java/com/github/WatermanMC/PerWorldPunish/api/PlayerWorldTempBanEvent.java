package com.github.WatermanMC.PerWorldPunish.api;

import java.util.UUID;

public class PlayerWorldTempBanEvent extends PerWorldPunishEvent {
    private final long minutes;
    private boolean cancelled = false;

    public PlayerWorldTempBanEvent(UUID playerId, String worldName, long minutes, String reason) {
        super(playerId, worldName, reason);
        this.minutes = minutes;
    }

    public long getMinutes() {
        return minutes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
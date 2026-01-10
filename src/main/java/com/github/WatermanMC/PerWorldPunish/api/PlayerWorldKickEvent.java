package com.github.WatermanMC.PerWorldPunish.api;

import java.util.UUID;

public class PlayerWorldKickEvent extends PerWorldPunishEvent {
    private boolean cancelled = false;

    public PlayerWorldKickEvent(UUID playerId, String worldName, String reason) {
        super(playerId, worldName, reason);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
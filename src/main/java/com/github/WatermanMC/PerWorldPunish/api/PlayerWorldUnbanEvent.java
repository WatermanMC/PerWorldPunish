package com.github.WatermanMC.PerWorldPunish.api;

import java.util.UUID;

public class PlayerWorldUnbanEvent extends PerWorldPunishEvent {
    public PlayerWorldUnbanEvent(UUID playerId, String worldName) {
        super(playerId, worldName, null);
    }
}
package com.github.WatermanMC.PerWorldPunish;

public class WorldBan {
    private String world;
    private String reason;
    private long expiryTime;
    private boolean isTemporary;

    public WorldBan(String world, String reason, long expiryTime, boolean isTemporary) {
        this.world = world;
        this.reason = reason;
        this.expiryTime = expiryTime;
        this.isTemporary = isTemporary;
    }

    public WorldBan(String world, String reason) {
        this(world, reason, 0, false);
    }

    public String getWorld() {
        return world;
    }

    public String getReason() {
        return reason;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public long getRemainingTime() {
        if (!isTemporary) return 0;
        return Math.max(0, expiryTime - System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorldBan worldBan = (WorldBan) obj;
        return world.equalsIgnoreCase(worldBan.world);
    }

    @Override
    public int hashCode() {
        return world.toLowerCase().hashCode();
    }
}
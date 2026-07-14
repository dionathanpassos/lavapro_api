package com.dionathan.lavapro.serviceOrder;

public enum ServiceOrderStatus {
    WAITING,
    IN_PROGRESS,
    READY,
    DELIVERED,
    CANCELLED;

    public boolean allowsChanges() {
        return this != DELIVERED && this != CANCELLED;
    }
}

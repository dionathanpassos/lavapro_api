package com.dionathan.lavapro.report.dto;

public record ServiceOrderSummaryDTO(
        Long waiting,
        Long inProgress,
        Long ready,
        Long delivered,
        Long canceled
) {
    public Long totalServiceOrder() {
        return (waiting != null ? waiting : 0L)
                + (inProgress != null ? inProgress : 0L)
                + (ready != null ? ready : 0L)
                + (delivered != null ? delivered : 0L)
                + (canceled != null ? canceled : 0L);
    }

    public Long totalServiceOrderInAction() {
        return (waiting != null ? waiting : 0L)
                + (inProgress != null ? inProgress : 0L)
                + (ready != null ? ready : 0L);
    }
}

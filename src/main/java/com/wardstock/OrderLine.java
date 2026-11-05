package com.wardstock;

public record OrderLine(
        Product product,
        Long amc,
        Long maxStock,
        Long closing,
        long suggestedQuantity,
        Status status) {

    public enum Status {
        OK,
        MANUAL_REVIEW,
        NO_REPORT,
        INVALID_REPORT
    }
}

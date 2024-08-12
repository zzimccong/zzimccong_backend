package com.project.zzimccong.model.dto.payment;

public enum PayType {
    CARD("CARD"),
    CASH("현금"),
    POINT("포인트");

    private String description;

    PayType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

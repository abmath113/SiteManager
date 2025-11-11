package com.eps.sitemanager.utilities.rentcalculation;

public enum PaymentInterval {

    MONTHLY(1),
    QUARTERLY(3),
    HALF_YEARLY(6),
    RENT_FREE(0);

    private final int value;

    PaymentInterval(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PaymentInterval fromValue(int value) {
        for (PaymentInterval interval : values())
            if (interval.value == value) return interval;

        throw new IllegalArgumentException("Invalid payment interval: " + value);
    }

}

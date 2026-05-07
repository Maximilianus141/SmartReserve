package ch.kenner.maximilian.smartreserve.model.reservation;

public enum ReservationStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED");

    public final String value;

    private ReservationStatus(String value) {
        this.value = value;
    }
}
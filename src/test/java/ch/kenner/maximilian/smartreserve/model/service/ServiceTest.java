package ch.kenner.maximilian.smartreserve.model.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * LB1 – Auftrag 1, Kategorie «Kombinatorische Logik».
 *
 * <p>Testet {@link Service#getWholeDurationSeconds()}: die Gesamtdauer eines
 * Services ergibt sich aus der Kombination von Dauer und Pausendauer.
 */
class ServiceTest {

    @Test
    void wholeDurationIsSumOfDurationAndBreak() {
        Service s = new Service();
        s.setDurationSeconds(180L);
        s.setAfterServiceBreakDurationSeconds(20L);
        assertEquals(200L, s.getWholeDurationSeconds());
    }

    @Test
    void wholeDurationWithoutBreakIsJustDuration() {
        Service s = new Service();
        s.setDurationSeconds(180L);
        s.setAfterServiceBreakDurationSeconds(0L);
        assertEquals(180L, s.getWholeDurationSeconds());
    }

    @Test
    void wholeDurationWithDefaultBreakIsJustDuration() {
        Service s = new Service();
        s.setDurationSeconds(60L);
        // afterServiceBreakDurationSeconds bleibt auf dem Default-Wert 0L
        assertEquals(60L, s.getWholeDurationSeconds());
    }
}

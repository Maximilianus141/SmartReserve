package ch.kenner.maximilian.smartreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LB1 – Auftrag 1, Kategorie «Format &amp; Muster (Regex)».
 *
 * <p>LB1 – Auftrag 3 (Test-Driven-Development): Diese Tests wurden
 * <b>vor</b> der Implementierung von {@link ValidationService} geschrieben.
 * Sie decken einen konkreten Fehlerfall (ungültige Eingabewerte) sowie das
 * fehlende Feature (zentrale Eingabevalidierung) ab.
 *
 * <p>Pro Kategorie werden Positiv- und Negativtests sowie Grenzfälle
 * (Boundary-Value-Analysis) geprüft.
 */
class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    // ---------------------------------------------------------------
    // E-Mail
    // ---------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = {
            "max@example.com",
            "max.muster@example.co.uk",
            "max_muster+tag@example.ch",
            "a@b.ch"
    })
    void validEmailShouldPass(String email) {
        assertTrue(validationService.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "maxexample.com",          // kein @
            "max@example",             // keine TLD
            "@example.com",            // kein local-part
            "max@.com",                // leere Domain
            "max@example.c",           // TLD zu kurz (1 Zeichen)
            "max example@example.com"  // Leerzeichen
    })
    void invalidEmailShouldFail(String email) {
        assertFalse(validationService.isValidEmail(email));
    }

    @Test
    void nullEmailShouldReturnFalse() {
        assertFalse(validationService.isValidEmail(null));
    }

    // ---------------------------------------------------------------
    // PLZ
    // ---------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = {"4051", "8000", "1000", "9999"})
    void validPlzShouldPass(String plz) {
        assertTrue(validationService.isValidPlz(plz));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "051",      // zu kurz
            "40512",    // zu lang
            "0512",     // führende Null
            "405A",     // Buchstabe
            "40 51",    // Leerzeichen
            ""          // leer
    })
    void invalidPlzShouldFail(String plz) {
        assertFalse(validationService.isValidPlz(plz));
    }

    // ---------------------------------------------------------------
    // IBAN (Schweiz)
    // ---------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = {
            "CH9300762011623852957",
            "CH1234567890123456789"
    })
    void validIbanShouldPass(String iban) {
        assertTrue(validationService.isValidIban(iban));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "DE89370400440532013000",   // falsches Länderkürzel
            "CH930076201162385295",     // zu kurz (20 Zeichen)
            "CH93007620116238529571",   // zu lang (22 Zeichen)
            "ch9300762011623852957",    // Kleinbuchstaben
            "CH93 0076 2011 6238 52957" // Leerzeichen
    })
    void invalidIbanShouldFail(String iban) {
        assertFalse(validationService.isValidIban(iban));
    }

    // ---------------------------------------------------------------
    // Passwort
    // ---------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = {
            "Passwort1!",
            "Abcdef12#",
            "Sicher1234$"
    })
    void validPasswordShouldPass(String password) {
        assertTrue(validationService.isValidPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "passwort1!",   // kein Grossbuchstabe
            "PASSWORT1!",   // kein Kleinbuchstabe
            "Passwort!",    // keine Ziffer
            "Passwort1",    // kein Sonderzeichen
            "Pas1!",        // zu kurz
            ""              // leer
    })
    void invalidPasswordShouldFail(String password) {
        assertFalse(validationService.isValidPassword(password));
    }
}

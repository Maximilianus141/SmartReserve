package ch.kenner.maximilian.smartreserve.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Zentrale Validierung von Eingabewerten anhand von regulären Ausdrücken.
 *
 * <p>Diese Klasse wurde im Rahmen von LB1 (Modul 450) mittels
 * Test-Driven-Development erstellt: Die zugehörigen Unit-Tests
 * ({@code ValidationServiceTest}) wurden <b>vor</b> der Implementierung
 * geschrieben (test-first, RED → GREEN).
 */
@Service
public class ValidationService {

    /** E-Mail: local-part@domain.tld, TLD mit mindestens 2 Buchstaben. */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /** Schweizer PLZ: exakt 4 Ziffern, erste Ziffer 1–9 (also 1000–9999). */
    private static final Pattern PLZ_PATTERN =
            Pattern.compile("^[1-9][0-9]{3}$");

    /** Schweizer IBAN: CH + 2 Prüfziffern + 17 alphanumerische Zeichen (total 21). */
    private static final Pattern IBAN_PATTERN =
            Pattern.compile("^CH[0-9]{2}[A-Z0-9]{17}$");

    /**
     * Passwort: mindestens 8 Zeichen, davon mindestens je ein Gross- und
     * Kleinbuchstabe, eine Ziffer und ein Sonderzeichen.
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPlz(String plz) {
        return plz != null && PLZ_PATTERN.matcher(plz).matches();
    }

    public boolean isValidIban(String iban) {
        return iban != null && IBAN_PATTERN.matcher(iban).matches();
    }

    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
}

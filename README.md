# SmartReserve

SmartReserve ist eine moderne Buchungsplattform für Dienstleistungen, entwickelt mit Spring Boot. Das System ermöglicht es Gästen, Termine zu buchen, während Administratoren die volle Kontrolle über alle Reservierungen und Services haben.

## Features

- **Gäste-Portal**:
    - Abrufen verfügbarer Reservierungen.
    - Buchung neuer Termine mit automatischer Konfliktprüfung.
    - Stornierung eigener Reservierungen.
- **Admin-Dashboard**:
    - Verwaltung aller Reservierungen (Erstellen, Aktualisieren, Löschen).
    - Übersicht über alle Buchungen im System.
- **Sicherheit**:
    - Integration mit Keycloak für OAuth2/OIDC Authentifizierung.
    - Rollenbasierte Zugriffskontrolle (RBAC) für Gäste und Administratoren.
- **Präzises Zeitmanagement**:
    - Durchgängige Nutzung von `ZonedDateTime` zur Vermeidung von Zeitzonen-Problemen.
    - Intelligente Überlappungsprüfung bei Buchungen.

## Konfiguration (Local Setup)

- **Datenbank-Name**: `m-295-smartreserve`
- **Keycloak**:
    - Der Realm-Export befindet sich im Projektverzeichnis als `smartreserve-realm.json`.
    - Realm: `smartreserve`
    - Verfügbare Rollen: `ROLE_admin`, `ROLE_guest`

## Technologiestack

- **Backend**: Java 26, Spring Boot 4.0.x
- **Datenbank**: PostgreSQL (Entwicklung), H2 (Tests)
- **Sicherheit**: Spring Security, OAuth2 Resource Server (Keycloak)
- **API-Dokumentation**: SpringDoc OpenAPI (Swagger UI)
- **Persistence**: Spring Data JPA / Hibernate

## Voraussetzungen

- **Java 26** oder höher
- **Maven**
- **PostgreSQL** Instanz
- **Keycloak** (für die Authentifizierung konfiguriert)

## Installation & Setup

1.  **Datenbank vorbereiten**:
    Erstelle eine PostgreSQL-Datenbank namens `m-295-smartreserve`.

2.  **Konfiguration**:
    Passe die `src/main/resources/application.yml` bei Bedarf an (Datenbank-Credentials, Keycloak Issuer-URI).

3.  **Bauen**:
    ```bash
    ./mvnw clean install
    ```

4.  **Starten**:
    ```bash
    ./mvnw spring-boot:run
    ```

## API Dokumentation

Sobald die Anwendung läuft, ist die Swagger UI unter folgendem Link erreichbar:
[http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)

## Zeitzonen-Konzept

Das Projekt nutzt strikt `ZonedDateTime`, um sicherzustellen, dass Termine unabhängig vom Standort des Nutzers korrekt gespeichert und angezeigt werden. Die Geschäftslogik validiert Buchungen auf Überlappungen nach dem Prinzip:
`Startzeit < Bestehendes_Ende AND Endzeit > Bestehender_Start`.

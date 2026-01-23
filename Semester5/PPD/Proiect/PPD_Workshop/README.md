# PPD Workshop System - Sistem de Rezervări Concurente

Acest proiect implementează un sistem client-server multithreaded pentru gestionarea rezervărilor la atelierele unui lanț de centre culturale.
Sistemul gestionează concurența folosind mecanisme de sincronizare fine-grained și persistă datele într-o bază de date PostgreSQL.

## Cerințe de Sistem

* **Java Development Kit (JDK):** 
* **PostgreSQL:** 
* **Maven:** 
* **IDE** IntelliJ IDEA.

## Structura Proiectului

* `src/main/java/ppd`: Fisierele de rulat (`WorkshopServer`, `TestDriver`).
* `src/main/java/ppd/client`: Fisiere care definesc logica clienților (`ClientHandler`, `WorkshopClient`).
* `src/main/java/ppd/data`: Interacțiunea cu baza de date (`Database`) și modelul de date.
* `src/main/java/ppd/config`: Gestionarea configurărilor (`WorkshopConfig`, `TestScenarioConfig`).
* `src/main/java/ppd/dto`: Data transfer objects (`Reservation`, `Transaction`, `Response`, `Request`).
* `src/main/resources/utils`: Fisiere care definesc clase auxiliare, helper (`PeriodicChecker`).
* `src/main/resources`: Fișiere de configurare (`.csv`, `.properties`).

## Configurare și Instalare

### 1. Baza de Date
Asigurați-vă că PostgreSQL rulează. Creați baza de date și tabelele:

1.  Creați baza de date `workshop_db`.
2.  Rulați scriptul de creare (adaptat după structura claselor):
    ```sql
    CREATE TABLE IF NOT EXISTS reservations (
        id VARCHAR(50) PRIMARY KEY,
        client_name VARCHAR(100),
        cnp VARCHAR(20),
        center_id INT,
        workshop_id INT,
        hour INT,
        status VARCHAR(20),
        cost DOUBLE PRECISION,
        created_at TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS transactions (
        id VARCHAR(50) PRIMARY KEY,
        reservation_id VARCHAR(50),
        type VARCHAR(20),
        amount DOUBLE PRECISION,
        created_at TIMESTAMP,
        FOREIGN KEY (reservation_id) REFERENCES reservations(id)
    );
    ```
3.  Configurați credențialele în `src/main/java/ppd/data/Database.java` (variabilele `USER`, `PASS`).

### 2. Fișiere de Configurare (în `src/main/resources`)
Sistemul este flexibil și permite definirea scenariilor prin fișiere:

* **`workshops.csv`**: Definește tipurile de ateliere (ID, Nume, Cost, Durată).
* **`capacities.csv`**: Definește capacitatea fiecărei săli per centru (`C, W, Cap`).
* **`test_scenario1.properties sau test_scenario2.properties`**: Parametrii pentru `TestDriver` si `WorkshopServer`.
    ```properties
    clients_count=30
    test_duration_sec=240
    client_delay_ms=5000
    batch_size=10
    workshops_file = src/main/resources/workshops.csv
    capacities_file = src/main/resources/capacities.csv
    port = 9000
    thread_pool_size = 15
    check_interval_sec = 15
    ```

## Compilare in IntelliJ

Mergeti in fisierul WorkshopServer si apasati pe buton verde de "play" din stanga functiei main.
La fel procedati si pentru TestDriver, cele doua proiecte trebuie sa ruleze simultan.

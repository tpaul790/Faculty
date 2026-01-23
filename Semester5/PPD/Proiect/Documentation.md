# RAPORT DE PROIECT
## Programare Paralelă și Distribuită

**Titlu Proiect:** Sistem Client-Server Multithreaded pentru Gestiunea Rezervărilor (Workshop System)
**Student:** Titieni Robert-Paul
**Grupa:** 237
**Data:** 23.01.2026

---

## Cuprins

1.  [Introducere](#1-introducere)
2.  [Analiza Problemei](#2-analiza-problemei)
    * 2.1. Descrierea Scenariului
    * 2.2. Cerințe Funcționale și Non-Funcționale
3.  [Arhitectura Sistemului](#3-arhitectura-sistemului)
    * 3.1. Structura Generală
    * 3.2. Modelul de Date (Persistență)
    * 3.3. Protocolul de Comunicare
4.  [Implementare și Sincronizare](#4-implementare-și-sincronizare)
    * 4.1. Gestionarea Concurenței (Multithreading)
    * 4.2. Granularitatea Blocării (Lock Striping)
    * 4.3. Prevenirea Deadlock-ului și Starvation
    * 4.4. Consistența Datelor (Periodic Checker)
5.  [Analiza Performanței](#5-analiza-performanței)
    * 5.1. Scenariul de Testare
    * 5.2. Comparație: Serial vs. Concurent
6.  [Instrucțiuni de Utilizare](#6-instrucțiuni-de-utilizare)
7.  [Concluzii](#7-concluzii)

---

## 1. Introducere

În contextul sistemelor distribuite moderne, gestionarea accesului concurent la resurse partajate limitate reprezintă o provocare fundamentală. Acest proiect își propune dezvoltarea unui sistem de rezervări pentru un lanț de centre culturale, capabil să deservească simultan cereri multiple venite de la clienți distribuiți, garantând în același timp integritatea datelor și corectitudinea tranzacțiilor financiare.

Obiectivul principal este implementarea unui server concurent care să gestioneze corect problema **"Race Condition"** (condiții de cursă) care apare atunci când mai mulți clienți încearcă să rezerve ultimele locuri disponibile la un atelier.

---

## 2. Analiza Problemei

### 2.1. Descrierea Scenariului
Sistemul modelează un lanț de $n$ centre culturale. Fiecare centru oferă același set de $m$ ateliere (ex: Pictură, Ceramică), însă capacitățile sălilor diferă de la un centru la altul.

Un client poate efectua trei tipuri de operațiuni:
1.  **Rezervare:** Solicită un loc la un atelier specific, la o oră specifică.
2.  **Plată:** Confirmă o rezervare existentă prin transferul unei sume de bani.
3.  **Anulare:** Renunță la o rezervare plătită, solicitând restituirea sumei.

### 2.2. Cerințe Funcționale și Non-Funcționale
Conform specificațiilor:
* **Capacitate Limitată:** Numărul de participanți simultani la un atelier $W_j$ din centrul $C_i$ nu poate depăși capacitatea $Cap(i,j)$.
* **Validare Temporală:** Atelierele au durate variabile (ex: 2 ore). O rezervare la ora 10:00 pentru un atelier de 2 ore ocupă resurse și la ora 11:00.
* **Persistență:** Toate datele trebuie salvate într-o bază de date (PostgreSQL).
* **Verificare Periodică:** Un proces de fundal trebuie să verifice consistența sumelor încasate și să elimine rezervările neplătite în timp util (T_plata).

---

## 3. Arhitectura Sistemului

### 3.1. Structura Generală
Aplicația urmează o arhitectură **Client-Server** bazată pe socket-uri TCP/IP.

* **Serverul:** Este componenta pasivă care ascultă pe un port (9000). La acceptarea unei conexiuni, deleagă procesarea unui `ClientHandler` care rulează într-un **Thread Pool**. Acest lucru permite serverului să rămână receptiv la noi conexiuni chiar și sub sarcină mare.
* **Clientul (`TestDriver`):** Este componenta activă care simulează comportamentul uman, generând cereri aleatoare într-un flux continuu, respectând pauzele configurate (2s între cereri).

### 3.2. Modelul de Date (Persistență)
Baza de date relațională este utilizată pentru a stoca starea sistemului. Schema bazei de date (PostgreSQL) cuprinde:

1.  **Tabelul `reservations`:** Stochează detaliile rezervării (ID, Client, Centru, Atelier, Ora, Status, Cost).
    * Coloana `status` gestionează ciclul de viață: `PENDING` $\to$ `PAID` $\to$ `CANCELLED` sau `EXPIRED`.
2.  **Tabelul `transactions`:** Funcționează ca un jurnal financiar (ledger). Orice modificare financiară (plată sau refund) generează o intrare nouă. Aceasta permite verificarea auditabilă a sumelor (suma tranzacțiilor = suma costurilor rezervărilor plătite).

### 3.3. Protocolul de Comunicare
S-a utilizat serializarea obiectelor Java (`ObjectInputStream` / `ObjectOutputStream`).
* **Request:** Obiect ce încapsulează tipul cererii (RESERVE, PAY, CANCEL) și parametrii necesari.
* **Response:** Obiect ce conține succesul operațiunii (boolean), un mesaj descriptiv și date asociate (ex: ID-ul rezervării).

---

## 4. Implementare și Sincronizare

Aceasta este secțiunea critică a proiectului, tratând mecanismele PPD.

### 4.1. Gestionarea Concurenței (Multithreading)
Serverul utilizează `ExecutorService` cu un pool fix de fire de execuție ($p=8$, conform cerinței). Aceasta limitează numărul de fire active simultan, prevenind epuizarea resurselor sistemului (Context Switching excesiv) în cazul unui număr foarte mare de clienți.

### 4.2. Granularitatea Blocării (Lock Striping)
Pentru a asigura corectitudinea datelor fără a sacrifica performanța, am evitat utilizarea unui singur `lock` global (care ar fi serializat tot sistemul).

Am implementat **Fine-Grained Locking** (blocare fină) la nivel de resursă atomică: perechea **(Centru, Atelier)**.
* Am definit o matrice de lock-uri: `ReentrantLock[][] locks`.
* Atunci când un client vrea să rezerve *Pictură* la *Centrul 1*, obține doar `locks[1][Pictura]`.
* **Beneficiu:** Un alt client poate rezerva simultan *Ceramică* la *Centrul 1* sau *Pictură* la *Centrul 2*, deoarece lock-urile sunt distincte. Aceasta maximizează paralelismul.

### 4.3. Prevenirea Deadlock-ului și Starvation
* `ReentrantLock(true)` a fost inițializat cu parametrul `fairness = true`. Aceasta garantează că firele de execuție care așteaptă cel mai mult primesc accesul primele, prevenind **Starvation**.
* În cazul operațiunilor complexe care necesită multiple resurse (ex: `PeriodicChecker`), lock-urile sunt achiziționate întotdeauna într-o ordine predefinită (de la indexul 1 la N), eliminând posibilitatea de **Deadlock circular**.

### 4.4. Consistența Datelor (Periodic Checker)
O cerință specială a fost procesul de verificare automată.
* Acesta rulează pe un fir separat (`ScheduledExecutorService`) la fiecare 5 secunde.
* Pentru a efectua o verificare corectă ("Snapshot Isolation"), Checker-ul apelează metoda `lockAll()`. Aceasta blochează temporar toate atelierele, interoghează baza de date pentru a calcula sumele totale și verifică dacă există suprapuneri de capacitate.
* Dacă suma încasată reală diferă de suma așteptată, sau dacă `active_users > capacity`, eroarea este logată în `verification_report.txt`.

---

## 5. Analiza Performanței

### 5.1. Scenariul de Testare
Testele au fost efectuate respectând parametrii:
* Număr clienți: 20
* Durată test: 120 secunde
* Frecvență cereri: 1 cerere / 2 secunde per client
* Thread-uri server: 8

### 5.2. Comparație: Serial vs. Concurent

| Metrica | Implementare Serială (Estimare) | Implementare Concurentă (Actuală) |
| :--- | :--- | :--- |
| **Mecanism Sincronizare** | `synchronized(this)` pe toată metoda `processRequest` | `ReentrantLock` per `(Center, Workshop)` |
| **Comportament** | Un singur client servit la un moment dat. | Clienții pe ateliere diferite sunt serviți paralel. |
| **Timp Mediu Răspuns** | Ridicat (crește liniar cu nr. clienți). | Scăzut (depinde doar de congestia locală). |
| **Throughput (Req/sec)** | Scăzut (limitat de I/O DB secvențial). | Ridicat (limitat de conexiunile DB și CPU). |
| **Utilizare Resurse** | Subutilizare CPU (multe nuclee stau degeaba). | Utilizare eficientă a multiplelor nuclee. |

**Rezultate observate:**
În timpul rulării testului de 120s, sistemul a procesat cu succes toate cererile. Fișierul de log `verification_report.txt` a confirmat că:
1.  Nu au existat suprapuneri (Capacity Overflow).
2.  Balanța financiară a fost mereu corectă (diferența 0.00).

---

## 6. Instrucțiuni de Utilizare

Pentru a reproduce testele și a rula aplicația:

1.  **Configurare Mediu:**
    * Asigurați-vă că aveți Java 17+ și Maven instalate.
    * Porniți serverul PostgreSQL și creați baza de date `workshop_db`.
    * Verificați credențialele în `src/main/java/ppd/data/Database.java`.

2.  **Configurare Scenariu:**
    * Editați `src/main/resources/test_scenario.properties` pentru a modifica numărul de clienți sau durata.
    * Editați `workshops.csv` și `capacities.csv` pentru a schimba datele de domeniu.

3.  **Rulare:**

    *Compilare:*
    ```bash
    mvn clean package
    ```

    *Pornire Server:*
    ```bash
    java -cp target/classes:target/dependency/* ppd.server.WorkshopServer
    ```

    *Pornire Client (Test Driver):*
    ```bash
    java -cp target/classes:target/dependency/* ppd.TestDriver
    ```

4.  **Verificare:**
    * Urmăriți consola pentru log-uri în timp real.
    * Deschideți `verification_report.txt` la final pentru raportul de audit.

---

## 7. Concluzii

Proiectul demonstrează implementarea cu succes a unui sistem distribuit robust. Utilizarea mecanismelor de sincronizare avansate (`ReentrantLock`, `ScheduledExecutorService`) a permis gestionarea eficientă a concurenței, asigurând integritatea datelor impusă de scenariul "Workshop".

Arhitectura modulară și separarea clară între logica de business, accesul la date și configurare permit extinderea ușoară a sistemului (de exemplu, adăugarea mai multor tipuri de resurse sau migrarea către un alt tip de bază de date) fără a rescrie logica de sincronizare.

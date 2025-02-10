INSERT INTO Pacienti VALUES
    ('Titieni','Paul','default'),('Costan','Alex','default'),
    ('Stiube','Denis','default'),('Tobias','Raul','default');

INSERT INTO Specializari VALUES
    ('dermatologie'),('stomatologie'),('orologie'),('neurochirurgie');

INSERT INTO Diagnostice VALUES
    ('scabie','default'),('adenom hipofizal','default'),('tiroida','default'),
    ('piele uscata','default'),('carie','default'),('plomba','default');

INSERT INTO Medici VALUES
    ('Pasc','Alex','dermatologie'),('Florian','Andrei','neurochirurgie'),
    ('Dragus','Marin','stomatologie'),('Ursu','Toader','orologie');

INSERT INTO Consultatii VALUES
    (1,1,'2025.01.01','9:00','scabie','default'),
    (1,2,'2025.01.01','10:00','scabie','default'),
    (1,3,'2025.01.02','11:00','scabie','default'),
    (1,1,'2025.01.03','9:00','scabie','default'),
    (1,2,'2025.01.03','10:00','scabie','default');
INSERT INTO Consultatii VALUES
    (2,1,'2025.01.11','9:00','carie','default'),
    (2,2,'2025.01.11','10:00','carie','default'),
    (2,3,'2025.01.12','11:00','carie','default'),
    (2,1,'2025.01.13','9:00','carie','default'),
    (2,2,'2025.01.13','10:00','carie','default');

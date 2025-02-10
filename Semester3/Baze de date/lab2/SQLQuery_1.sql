USE CompanieCurierat;
Delete from Soferi;


SELECT * FROM Soferi;
Delete from Soferi;
DBCC CHECKIDENT ('Soferi', RESEED,0);
INSERT INTO Soferi VALUES('Ionut',7,'B',7000);
INSERT INTO Soferi VALUES('Florina',12,'C',9000);
INSERT INTO Soferi VALUES('Paul',3,'B',6500);
INSERT INTO Soferi VALUES('Emilian',10,'C',8700);
INSERT INTO Soferi VALUES('Alex',5,'B',4200);
INSERT INTO Soferi VALUES('Denis',7,'C',7900);
INSERT INTO Soferi VALUES('Andrei',10,'D',11000);


SELECT * FROM Adresa;
DBCC CHECKIDENT ('Adresa', RESEED,0);
Delete from Adresa;
INSERT INTO Adresa VALUES('B-N','Santioana','Principala',161,NULL,NULL);
INSERT INTO Adresa VALUES('B-N','Bistrita','Tarpiului',10,NULL,NULL); 
INSERT INTO Adresa VALUES('C-N','Cluj','Tulcea',21,'K2',47); 
INSERT INTO Adresa VALUES('B','Bucuresti','Independetei',84,NULL,NULL); 
INSERT INTO Adresa VALUES('CV','Craiova','Universitatii',32,NULL,NULL); 
INSERT INTO Adresa VALUES('BC','Bacau','Libertatii',12,'A3',31); 
INSERT INTO Adresa VALUES('AD','Arad','Decebal',1,'M12',120); 
INSERT INTO Adresa VALUES('SV','Suceava','Han',83,NULL,NULL); 
INSERT INTO Adresa VALUES('B-N','Principala','Sieu',32,NULL,NULL); 


SELECT * FROM Clienti; 
DBCC CHECKIDENT ('Clienti', RESEED,0);
Delete from Clienti;
INSERT INTO Clienti VALUES('Sergiu','sergiu31@gmail.com','0761234323','fizic');
INSERT INTO Clienti VALUES('Edy','edycugi83@gmail.com','0263260342','fizic');
INSERT INTO Clienti VALUES('Maria','mariii23@gmail.com','0763196245','fizic');
INSERT INTO Clienti VALUES('Ionela','nionela67@gmail.com','0784904928','fizic');
INSERT INTO Clienti VALUES('Ema','todaema5@gmail.com','0761234324','fizic');
INSERT INTO Clienti VALUES('Alexandra','alexandra@gmail.com','0263260122','fizic')
INSERT INTO Clienti VALUES('Delia','tapucd@gmail.com','0754396245','fizic');
INSERT INTO Clienti VALUES('Oana','toana83@gmail.com','07845674928','fizic');
INSERT INTO Clienti VALUES('Codrin','tcodrinel3@gmail.com','0761274223','fizic');


SELECT * FROM Livrari;
DBCC CHECKIDENT ('Livrari', RESEED,0);
Delete from Livrari;
INSERT INTO Livrari VALUES(2,1,'2024.10.06','In curs');
INSERT INTO Livrari VALUES(3,2,'2023.12.15','Livrat');
INSERT INTO Livrari VALUES(1,3,'2022.02.18','Livrat');
INSERT INTO Livrari VALUES(4,2,'2024.10.10','Tranzit');
INSERT INTO Livrari VALUES(3,3,'2024.10.08','Tranzit');
INSERT INTO Livrari VALUES(6,5,'2024.10.20','In curs');
INSERT INTO Livrari VALUES(5,9,'2023.12.11','Livrat');
INSERT INTO Livrari VALUES(7,9,'2023.12.12','Livrat');
INSERT INTO Livrari VALUES(3,5,'2024.10.22','Tranzit');


SELECT * FROM Masini;
Delete from Masini;
INSERT INTO Masini VALUES('BN04TIF','Audi',100,'Diesel',12000,1);
INSERT INTO Masini VALUES('BN84BIP','Range Rover',250,'Benzina',187500,4);
INSERT INTO Masini VALUES('CJ32MSB','Renault',180,'GPL',146900,2);
INSERT INTO Masini VALUES('MM12TRP','Mercedes',80,'Electricitate',50000,3);
INSERT INTO Masini VALUES('BH43STA','Audi',40,'Diesel',120000,6);
INSERT INTO Masini VALUES('CV23TAI','Skoda',132,'GPL',38000,7);
INSERT INTO Masini VALUES('BN32CAI','Mercedes',42,'Benzina',2000500,5);


SELECT * FROM Colet;
DBCC CHECKIDENT ('Colet', RESEED,0);
Delete from Colet;
INSERT INTO Colet VALUES(1,2,10,0.1,'2023.12.10','Livrat');
INSERT INTO Colet VALUES(3,5,15,0.4,'2024.10.02','Tranzit');
INSERT INTO Colet VALUES(3,3,12,0.2,'2022.02.11','Livrat');
INSERT INTO Colet VALUES(1,1,11,1,'2024.10.05','In livrare');
INSERT INTO Colet VALUES(4,4,9,0.6,'2024.10.03','Tranzit');
INSERT INTO Colet VALUES(8,8,3,0.2,'2023.12.11','Livrat');
INSERT INTO Colet VALUES(7,9,5,0.4,'2024.10.18','Tranzit');
INSERT INTO Colet VALUES(7,6,12,1.2,'2024.10.18','In curs');
INSERT INTO Colet VALUES(8,7,11,2,'2023.12.11','Livrat');


SELECT * FROM Retur;
DBCC CHECKIDENT ('Retur', RESEED,0);
Delete from Retur;
INSERT INTO Retur VALUES(1,'2023.10.12','Finalizat');
INSERT INTO Retur VALUES(3,'2022.03.05','Finalizat');


SELECT * FROM Depozite;
DBCC CHECKIDENT ('Depozite', RESEED,0);
Delete from Depozite;
INSERT INTO Depozite VALUES('FedLex',1200,'0781235342',4);
INSERT INTO Depozite VALUES('Emdlag',9450,'0766345897',6);
INSERT INTO Depozite VALUES('Amazan',13000,'0760917376',8);
INSERT INTO Depozite VALUES('FedLex2',15000,'0787052432',4);
INSERT INTO Depozite VALUES('Edelman2',3500,'0763412344',7);
INSERT INTO Depozite VALUES('Amazan3',6450,'0763513465',8);
INSERT INTO Depozite VALUES('Amazan2',7300,'0783457823',8);
INSERT INTO Depozite VALUES('Edelman',12340,'0762709523',7);
INSERT INTO Depozite VALUES('Emdlag2',8900,'0780247029',6);


SELECT * FROM Trasee;
DBCC CHECKIDENT ('Trasee', RESEED,0);
Delete from Trasee;
INSERT INTO Trasee VALUES(3,100,3,1);
INSERT INTO Trasee VALUES(2,230,7,2);
INSERT INTO Trasee VALUES(4,145,5,4);
INSERT INTO Trasee VALUES(1,294,8,1);
INSERT INTO Trasee VALUES(5,120,4,6);
INSERT INTO Trasee VALUES(7,60,2,4);
INSERT INTO Trasee VALUES(6,101,3,9);
INSERT INTO Trasee VALUES(3,98,4,8);
INSERT INTO Trasee VALUES(5,57,2,7);


SELECT * FROM Tranzit;
Delete from Tranzit;
INSERT INTO Tranzit VALUES(1,4,'2023.12.05','2023.12.07');
INSERT INTO Tranzit VALUES(1,3,'2023.12.07','2023.12.08');
INSERT INTO Tranzit VALUES(3,4,'2022.02.01','2022.02.08');
INSERT INTO Tranzit VALUES(3,3,'2022.02.08','2022.02.11');
INSERT INTO Tranzit VALUES(6,4,'2023.12.01','2023.12.05');
INSERT INTO Tranzit VALUES(6,3,'2023.12.05','2023.12.11');
INSERT INTO Tranzit VALUES(9,4,'2023.12.01','2023.12.05');
INSERT INTO Tranzit VALUES(9,3,'2023.12.05','2023.12.10');




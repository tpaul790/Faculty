## Java vs. C++
Folosind diferite metode pentru rezolvarea task-ului cu aplicarea unui kernel pe o matrice, am calculat o medie a timpului
de executie pentru 10 rulari ale aceleiasi abordari atat in cpp cat si in java, pentru diferite valori ale lui N,M si K.
Bazat pe aceste rezultate am tras urmatoarele concluzii.

Pentru ambele limbaje, procesul de calcul se bazeaza pe implementarea unei clase abstracte ("Convolution") in functie de metoda folosita: 
secvential sau paralel unde se adauga diferitele abordari: batch, block, linear, cyclic. Ce se masoara este timpul de executie
al metodei *convolve()* a carei implementare este diferita in functie de abordare iar lansarea ei in executie se face in functia main 
printr un apel polimorfic.

Din punct de vedere al performantei intre cele doua limbaje, abordarea in c++ cu matrice statica este cea mai ineficienta insa alocarea dinamica in
c++ se apropie de cea din java, alocarea realizandu-se in abele limbaje pe heap, fiind chiar mai eficienta pe seturi mici de date precum N=10, M=10
(0.01ms vs 0.07ms in cazul variantei secventiale), insa mult mai ineficienta pentru seturi mari precum N=10000, M=10000 (16s vs 2.2s in varianta secventiala)
Cand vine vorba de variantele paralele cpp ramane superior pentru cazuri cu foarte putine date ex: batch rows pe cazul N=10, m=10, P=4 am obtinut 0.9ms
in java si 0.05ms in cpp insa pentru seturi mari, java este mai rapid, de ex: batch columns pe cazul N=10000, M=10000, P=8 am obtinut 5.6s in java si
3.6s in cpp.

### Cazuri de testare
- N = M = 10, k = 3
- N = M = 1000, k = 5
- N = 10, M = 1000, k = 5
- N = 1000, M = 10, k = 5
- N = M = 10000, k = 5

### Metode ale implementarii paralele
- Batch Rows
- Batch Columns
- Block
- Distribution Linear
- Distribution Circular

## Rezultate C++

- Cand vine vorba de implementarea in c++ cea mai vizibila diferenta este faptul ca tipul matricei afecteaza foarte mult performanta.
- Pentru cazurile de testare diferite de cazul: N=10000, M=10000, in care alocarea este statica timpii de executie sunt mult inferiori
modelului cu alocare dinamica, de ex: pentru varianta secventiala, la matricea statica cu N=10 si M=10 am obtinut un timp mediu de 38ms
pe cand la matricea dinamica am obtinut la acelasi test case 0.01ms.
- In cazul testului cu N=10000 si M=10000, matricea satica este mai eficienta 16s comparativ cu 18s in cadrul matricei dinamice pe acelasi caz.
- Problemele matricii statice sunt cauzate de alocarea a 10000*10000 int-uri din start desi in marea majoritate a cazurilor nu am nevoie de atat
iar in cazul matricii dinamice apare problema dublei dereferentieri lucru resimtit in ultimul caz de testare.
- Cand vine vorba de abordarea paralela: batch, block, linear sau cyclic, diferentele sunt infime, aproape inexistente variind de la o rulare la alta.

## Rezultate Java

- Cel mai bun timp l-am obtinut, cum era si de asteptat, la cazul cu cele mai putine date si in varianta secventiala unde
  am obtinut un timp mediu de 0.07ms comparativ cu variantele parelele care se rotesc in jurul unei ms.
- Pentru cazurile paralele, cand vine vorba de acelasi caz de testare, diferentele sunt foarte mici si variaza de la
  o rulare la alta.
- De asemenea se observa ca un numar inadecvat de thread uri poate afecta negativ performanta, de ex:
  pentru cazul N=1000, M=1000, la toate abordarile paralele, 8 thread uri a fost un numar inadecvat acest lucru ducand
  la o performanta semnificativ mai mica, in comparatie cu 2 sau 4 thread uri care s-au dovedit a fi cele mai bune
  variante pentru acest caz indiferent de abordare
- Cea mai slaba performanta am obtinut-o la cazul cu cele mai multe date, N=10000, M=10000 unde la varianta secventiala
  a durat convolutia putin peste 2s in timp ce la variantele paralele, indiferent de metoda, daca numarul de thread uri
  este bine ales timpul poate fi de pana la 4x mai mic (in acest caz s-a dovedit a fi 8 sau 16).

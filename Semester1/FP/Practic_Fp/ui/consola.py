from service.service import Controler

class Consola:
    def __init__(self,controler:Controler) -> None:
        self.__controler=controler

    def print_meniu(self):
        """
        Se afiseaza meniul
        """
        print("1. Modifica data si genul unei melodii")
        print("2. Generare random")
        print("3. Export")
    
    def modificare_melodii_ui(self):
        """
        Functia citeste datele corespunzatoare apelului functiei
        modificare_melodii din controler si apeleaza aceasta functie
        """
        titlu=input("Introduceti titlul: ")
        artist=input("Introduceti artistul: ")
        gen=input("Introduceti genul: ")
        data=input("Introduceti data: ")
        try:
            titlu=str(titlu)
            artist=str(artist)
            gen=str(gen)
            data=str(data)
            self.__controler.modificare_melodii(titlu,artist,gen,data)
        except ValueError as e:
            print(str(e))

    def generare_melodii_ui(self):
        """
        Functia citeste datele corespunzatoare apelului functiei
        generare_melodii din controler si apeleaza aceasta functie
        """
        numar=input("Introduceti un numar: ")
        titluri=input("Introduceti titlurile: ")
        artisti=input("Introduceti artistii: ")
        try:
            numar=int(numar)
            titluri=str(titluri)
            artisti=str(artisti)
            adaugate=self.__controler.generare_melodii(numar,titluri,artisti)
            print(f"S-au adugat {adaugate} melodii")
        except ValueError as e:
            print(str(e))

    def export_ui(self):
        """
        Citeste numele unui fisier si realizeaza exportul in acel fisier
        """
        file_name=input("Introduceti numele fisierului: ")
        try:
            self.__controler.export(file_name)
        except ValueError as e:
            print(str(e))

    def start(self):
        while(True):
            self.print_meniu()
            optiune=input("Alegeti o optiune: ")
            if optiune=="1":
                self.modificare_melodii_ui()
            elif optiune=="2":
                self.generare_melodii_ui()
            elif optiune=="3":
                self.export_ui()
            else:
                print("Optiune invalida")

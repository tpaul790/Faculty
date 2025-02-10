from domain.entitati import Melodie

class MelodieInFileRepo:
    def __init__(self,file_name:str) -> None:
        self.__file_name=file_name
        self.__lista_melodii=[]
        self.load_from_file()
    
    def load_from_file(self):
        """
        Incarca toate melodiile din fisier in lista de melodii
        param:
        return:
        """
        try:
            f=open(self.__file_name,"r")
        except ValueError as e:
            print(str(e))
        linii=f.readlines()
        for linie in linii:
            linie=linie.strip().split("|")
            titlu=linie[0].strip()
            artist=linie[1].strip()
            gen=linie[2].strip()
            data=linie[3].strip()
            melodie=Melodie(titlu,artist,gen,data)
            self.store(melodie)
        f.close()

    def save_to_file(self):
        """
        Salveaza in fisier toate melodiile din lista de melodii
        param:
        return:
        """
        try:
            f=open(self.__file_name,"w")
        except ValueError as e:
            print(str(e))

        list=self.get_all()
        for melodie in list:
            titlu=melodie.get_titlu()
            artist=melodie.get_artist()
            gen=melodie.get_gen()
            data=melodie.get_data()
            string=f"{titlu} | {artist} | {gen} | {data}\n"
            f.writelines(string)
        f.close()
    
    def store(self,melodie:Melodie):
        """
            Adauga o melodie data in lista de melodii
            melodie: melodia care se va adauga
            return: 
        """
        self.__lista_melodii.append(melodie)
        self.save_to_file()
    
    def find(self,titlu:str,artist:str):
        """
        Verifica daca exista o melodie cu titlul si artistul dat
        titlu: titlul care se cauta
        artist: artistul care se cauta
        return: melodia cu titlul si artistul dat sau none
        """
        list=self.get_all()
        for melodie in list:
            if melodie.get_titlu()==titlu and melodie.get_artist()==artist:
                return melodie
        return None

    def get_all(self):
        """
        returneaza lista de melodii
        """
        return self.__lista_melodii

    def clear_all(self):
        """
        goleste lista cu melodii
        """
        self.__lista_melodii=[]
        self.save_to_file()


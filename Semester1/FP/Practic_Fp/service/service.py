import sys
sys.path.append("/Users/titienipaul/Desktop/Practic_Fp")
from domain.entitati import Melodie
from repository.melodie_repo import MelodieInFileRepo
from domain.validari import Validator
import random
from domain.utils import merge_sort


class Controler:
    def __init__(self,repo:MelodieInFileRepo) -> None:
        self.__repo=repo

    def modificare_melodii(self,titlu:str,artist:str,gen:str,data:str):
        """
        Functia modifica genul si data melodiei cu titlul si artistul dat
        titlu: titlul melodiei pentru care se vrea modificare
        artist: artistul melodiei pentru care se vrea modificare
        gen: noul gen al melodiei
        data: noua data a melodiei
        return:
        """
        validator=Validator
        melodie=self.__repo.find(titlu,artist)
        if melodie==None:
            raise ValueError("Nu exista o melodie cu titlul si artistul dat")
        validator.validare_melodie(gen,data)
        melodie.set_data(data)
        melodie.set_gen(gen)
        self.__repo.save_to_file()
    
    def generare_melodii(self,numar:int,lista_titluri:str,lista_artisti:str)->int:
        """
        Functia genereaza si stocheaza in fisier un numar dat de melodii
        numar: numarul de melodii care vor fi generate
        lista_titluri: titlurile din care se vor alege titlurile pentru meldoii
        lista_artisti: artisitii din care se vor alege artistii pentru melodii
        return: numarul de melodii adaugate
        """
        titluri=lista_titluri.split(",")
        l_titluri=len(titluri)
        artisti=lista_artisti.split(",")
        l_artisti=len(artisti)
        genuri=["Rock","Pop","Jazz"]
        numar_generari_posibile=l_titluri*l_artisti
        contor=0
        while numar!=0 and numar_generari_posibile!=0:
            titlu=random.choice(titluri)
            artist=random.choice(artisti)
            gen=random.choice(genuri)

            zi=str(random.randint(1,28))
            luna=str(random.randint(1,12))
            an=str(random.randint(1900,2024))
            data=f"{zi}.{luna}.{an}"
            melodie=self.__repo.find(titlu,artist)
            if melodie==None:
                melodie=Melodie(titlu,artist,gen,data)
                numar-=1
                numar_generari_posibile-=1
                contor+=1
                self.__repo.store(melodie)
        return contor

    def export(self,file_name:str):
        """
            Functia scrie in fisierul cu numele dat toate melodiile
            sortate crescator dupa data
            file_name: numele fisierului in care se vor scrie melodiile
            return: 
        """
        lista=self.get_all()
        lista=merge_sort(lista)
        try:
            f=open(file_name,"w")
        except ValueError as e:
            print(str(e))

        for melodie in lista:
            titlu=melodie.get_titlu()
            artist=melodie.get_artist()
            gen=melodie.get_gen()
            data=melodie.get_data()
            string=f"{titlu} | {artist} | {gen} | {data}\n"
            f.writelines(string)
        f.close()

    def get_all(self):
        """
        returneaza lista cu toate melodiile
        """
        return self.__repo.get_all()

